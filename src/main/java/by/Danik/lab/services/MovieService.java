package by.Danik.lab.services;

import by.Danik.lab.SimpleCache;
import by.Danik.lab.exceptions.CustomException;
import by.Danik.lab.models.ResponseDataToClient;
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

/**
 * Получить данные о фильмах из API кинопоиска
 */
@Service
@Slf4j
public class MovieService {

    private final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/search?";     // API кинопоиска
    private final String PAGE = "&page=";     // ответ может быть на несколько страниц, номер текущей страницы
    private final String LIMIT = "&limit=";     // сколько элементов на страницу, значение 1 значит забрать только один элемент
    private final String TITLE = "&query=";     // название фильма

    @Autowired
    private RestTemplate restTemplate;                                    // клиент для HTTP запросов
    @Autowired
    private ObjectMapper objectMapper;                                   // сериализация/десериализация (класс из библиотеки Jackson)
    @Autowired
    private SimpleCache<MovieRequestParams, String>  simpleCache;        // кеширование
    @Autowired
    private MovieRepository movieRepository;                            // репозиторий фильмов
    @Autowired
    private GenreRepository genreRepository;                            // репозиторий жанров
    @Autowired
    private CountryRepository countryRepository;                        // репозиторий стран

    /**
     * Пришёл запрос на фильмы методом GET
     * @param movieRequestParams - параметры запроса
     * @return
     */
    public String methodGet(MovieRequestParams movieRequestParams) {
        return getMovieByTitle(movieRequestParams);
    }

    /**
     * Обработка bulk запроса
     * метод POST
     * @param titles
     * @return
     */
    public String methodPostBulk(List<String> titles){

        // stream - превращает в поток данных
        // map - один из методов stream, принимает параметр лямбда-выражение, создаёт новый поток, не изменяя исходный
        // toList() - собирает всё что вернёт map в ArrayList
        return titles.stream()
                //лямбда выражение, для каждого элемента списка вызвать метод getMovieByTitle и передать параметры запроса
                .map(title -> getMovieByTitle(new MovieRequestParams(title)))
                .toList().toString();
    }

    /**
     * Получить фильм (фильмы) по их имени
     * @param movieRequestParams - параметры запроса
     * @return ResponseDataToClient - объект ответа клиенту
     */
    private String getMovieByTitle(MovieRequestParams movieRequestParams) {

        // json ответ от сервера
        String responseJsonFromApi = getMoviesFromAPi(movieRequestParams);

        // json ответ от сервера преобразуем в объект
        ResponseDataFromApi<Movie> responseDataFromApi = deserializeResponseData(responseJsonFromApi);

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

        return serializeResponseData(movies);
    }

    /**
     * Получить список фильмов по заданным параметрам
     * @param movieRequestParams - параметры запроса
     * @return json ответ
     */
    private String getMoviesFromAPi(MovieRequestParams movieRequestParams) {
        logger.error("параметры запроса " + movieRequestParams.toString());
        // проверяем возможно есть в кеше
        if(simpleCache.containsKey(movieRequestParams)) {
            logger.info("Ответ был отдан из кеша");
            return simpleCache.get(movieRequestParams);
        }

        // get запрос на API кинопоиска с полученными от клиента параметрами
        String jsonMovies = restTemplateGet(movieRequestParams);

        //кешируем ответ
        simpleCache.put(movieRequestParams, jsonMovies);

        return jsonMovies;
    }

    /**
     * Запрос к API кинопоиска
     * @param movieRequestParams - параметры запроса, которые пришли от клиента
     * @return json ответ
     */
    private String restTemplateGet(MovieRequestParams movieRequestParams) {

        String url = BASE_URL + PAGE + movieRequestParams.getPage() + LIMIT + movieRequestParams.getLimit() + TITLE + movieRequestParams.getTitle();

        // ответ от кинопоиска
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        logger.info("был выполнен запрос к API кинопоиска");
        // тело ответа
        return response.getBody();
    }

    /**
     * Десериализация ответа от API кинопоиска
     * @param jsonString - json ответ
     * @return десериализованные данные ответа
     */
    private ResponseDataFromApi<Movie> deserializeResponseData(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<ResponseDataFromApi<Movie>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Ошибка десериализации json ответа от API кинопоиска: " + jsonString);

            throw CustomException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Внутренняя ошибка сервера")
                    .build();
        }
    }

    /**
     * Сериализация фильмов для отправки клиенту
     * @param movies - фильмы
     * @return - json строка
     */
    private String serializeResponseData(List<Movie> movies) {
        try {
            return objectMapper.writeValueAsString(movies);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка сериализации списка фильмов для ответа клиенту: " + movies.toString());

            throw CustomException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Внутренняя ошибка сервера")
                    .build();
        }
    }

    /**
     * API кинопоиска иногда возвращает некорректные результаты,
     * возвращает поле, где название фильма пустое
     * причина - ошибочная запись в их базе данных
     * надо такое удалить
     */
    private List<Movie> clearMovie(List<Movie> movies) {
        // лямбда-выражение для удаления элементов из коллекции (Добавлена в java 8)
        if (movies.removeIf(temp -> temp.getName() == null || temp.getName().isEmpty())) {
            logger.info("был удалён как минимум один фильм с пустым именем");
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