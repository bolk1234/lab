package by.Danik.lab.services;

import by.Danik.lab.cache.SimpleCache;
import by.Danik.lab.exceptions.CustomException;
import by.Danik.lab.models.RequestCounter;
import by.Danik.lab.models.movie.entities.Country;
import by.Danik.lab.models.movie.entities.Genre;
import by.Danik.lab.models.movie.entities.Movie;
import by.Danik.lab.models.ResponseDataFromApi;
import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.repositories.CountryRepository;
import by.Danik.lab.repositories.GenreRepository;
import by.Danik.lab.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Получить данные о фильмах из API кинопоиска
 */
@Service
@Slf4j
public class MovieService {

    @Autowired
    MovieApiService movieApiService;                                    // подключение к API кинопоиска
    @Autowired
    private RequestCounter requestCounter;                              // счётчик обращений к текущему сервису
    @Autowired
    private SimpleCache<String, List<Movie>> movieSimpleCache;        // кеширование фильмов title/фильмы из АПИ
    @Autowired
    private MovieRepository movieRepository;                            // репозиторий фильмов
    @Autowired
    private GenreRepository genreRepository;                            // репозиторий жанров
    @Autowired
    private CountryRepository countryRepository;                        // репозиторий стран

    @Autowired
    ExecutorService executorService;


    /**
     * Пришёл запрос на фильмы методом GET
     * @param movieRequestParams - параметры запроса
     * @return
     */
    public List<Movie> methodGet(MovieRequestParams movieRequestParams) {
        // увеличиваем счётчик запросов сервиса
        executorService.submit(requestCounter::increment);
        return getMovieByTitle(movieRequestParams);
    }

    /**
     * Обработка bulk запроса
     * метод POST
     * @param titles
     * @return
     */
    public List<List<Movie>> methodPostBulk(List<String> titles){

        // Bulk запрос считаем как один, не смотря что внутри несколько запросов
        executorService.submit(requestCounter::increment);

        // stream - превращает в поток данных
        // map - один из методов stream, принимает параметр лямбда-выражение, создаёт новый поток, не изменяя исходный
        // toList() - собирает всё что вернёт map в ArrayList
        return titles.stream()
                //лямбда выражение, для каждого элемента списка вызвать метод getMovieByTitle и передать параметры запроса
                .map(title -> getMovieByTitle(new MovieRequestParams(title)))
                .toList();
    }

    /**
     * Получить фильм (фильмы) по их имени
     * @param movieRequestParams - параметры запроса
     * @return json строка
     */
    private List<Movie> getMovieByTitle(MovieRequestParams movieRequestParams) {

        // проверяем возможно есть в кеше
        if(movieSimpleCache.containsKey(movieRequestParams.getTitle())) {
            logger.info("Ответ был отдан из кеша");
            return movieSimpleCache.get(movieRequestParams.getTitle());
        }

        // json ответ от сервера
        String responseJsonFromApi = movieApiService.restTemplateGet(movieRequestParams);

        // json ответ от сервера преобразуем в объект
        ResponseDataFromApi<Movie> responseDataFromApi =  movieApiService.deserializeResponseData(responseJsonFromApi);

        //очистка от мусора
        List<Movie> movies = clearMovie(responseDataFromApi.getDocs());

        // если фильмов не осталось, значит ничего хорошего не нашлось
        if(movies.isEmpty()) {
            logger.info("По запросу ничего не нашлось");
        } else {
            // сохраняем в базу
            saveMovies(movies);
            logger.info("Количество фильмов в ответе =" + movies.size());
        }

        //кешируем ответ
        movieSimpleCache.put(movieRequestParams.getTitle(), movies);

        return movies;
    }



    /**
     * API кинопоиска иногда возвращает некорректные результаты,
     * возвращает поле, где название фильма пустое
     * причина - ошибочная запись в их базе данных
     * надо такое удалить
     * примеры запроса: aas, sd
     */
    private List<Movie> clearMovie(List<Movie> movies) {
        // собираем фильмы с пустыми называниями
        List<Movie> removedMovies = movies.stream()
                .filter(temp -> temp.getName() == null || temp.getName().isEmpty())
                .toList();

        if (!removedMovies.isEmpty()) {
            removedMovies.forEach(movie -> logger.info("был удалён фильм с пустым именем, id в базе кинопоиска: " + movie.getKinoPoiskId()));
            movies.removeAll(removedMovies);
        }
        return movies;
    }

    /**
     * Сохранить все найденные фильмы в базе
     * из-за того что достаём id из базы для жанров и стран, значения просто перезаписываются и нет дублирования
     * @param movies - фильмы для записи в базу
     */
    @Transactional
    private void saveMovies(List<Movie> movies) {
        movies.forEach(this::updateGenreAndCountryInMovie);

        movieRepository.saveAll(movies);
    }

    /**
     * Обновить жанры и страны в фильме
     * Если этого не сделать они будут дублироваться
     * @param movies
     */
    private void updateGenreAndCountryInMovie(Movie movies) {
        List<Genre> genres = processGenres(movies.getGenres());
        List<Country> countries = processCountries(movies.getCountries());

        movies.setGenres(genres);
        movies.setCountries(countries);
    }

    /**
     * Жанры имеют с фильмами связь многие ко многим
     * Надо проверить если уже есть жанр в базе, сохраняем только если ещё в базе нет
     * @param inputGenres - список жанров
     * @return список стран, если страна в базе есть у неё будет известен id
     */
    private List<Genre> processGenres(List<Genre> inputGenres) {
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : inputGenres) {
            Genre existingGenre = genreRepository.findByName(genre.getName());
            if (existingGenre == null) {
                genres.add(genreRepository.save(genre));
            } else {
                genres.add(existingGenre);
            }
        }
        return genres;
    }

    /**
     * Страны имеют с фильмами связь многие ко многим
     * Надо проверить если уже есть страна в базе, сохраняем только если ещё в базе нет
     * @param inputCountries - список стран
     * @return список стран, если страна в базе есть у неё будет известен id
     */
    private List<Country> processCountries(List<Country> inputCountries) {
        List<Country> countries = new ArrayList<>();
        for (Country country : inputCountries) {
            Country existingCountry = countryRepository.findByName(country.getName());
            if (existingCountry == null) {
                countries.add(countryRepository.save(country));
            } else {
                countries.add(existingCountry);
            }
        }
        return countries;
    }
}