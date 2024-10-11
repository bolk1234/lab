package by.Danik.lab.services;

import by.Danik.lab.exceptions.InternalServerExceptionHandler;
import by.Danik.lab.exceptions.JsonProcessingExceptionHandler;
import by.Danik.lab.exceptions.NotFoundException;
import by.Danik.lab.models.movie.Movie;
import by.Danik.lab.models.ResponseData;
import by.Danik.lab.models.movie.MovieRequestParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Получить данные о фильмах из API кинопоиска
 */
@Service
public class MovieService {
    private final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/search?"; // API кинопоиска
    private final String PAGE = "&page=";     // ответ может быть на несколько страниц, номер текущей страницы
    private final String LIMIT = "&limit=";     // сколько элементов на страницу, значение 1 значит забрать только один элемент
    private final String TITLE = "&query=";     // название фильма
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MovieService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Получить фильм (фильмы) по их имени
     * @param movieRequestParams - параметры запроса
     * @return
     */
    public String getMovieByTitle(MovieRequestParams movieRequestParams) throws JsonProcessingException {

        // json ответ от сервера
        String responseJsonFromApi = getMoviesFromAPi(movieRequestParams);
        // ответ от сервера в виде объекта
        ResponseData<Movie> responseDataFromApi = deserializeResponseData(responseJsonFromApi);

        //TODO можно regexp проверить на точное совпадение, потому что кинопоиск ищет по неполному совпадению
       /// MovieNotFoundException

        //очистка от мусора
        List<Movie> movies = clearMovie(responseDataFromApi.getDocs());

        // если фильмов не осталось, значит ничего хорошего не нашлось
        if(movies.isEmpty()) {
           throw new NotFoundException("По вашему запросу ничего не найдено", 400);
        }

        // json ответ клиенту, фильмы, без лишних полей
        return serializeResponseData(movies);
    }

    /**
     * Получить список фильмов по заданным параметрам
     * @param movieRequestParams - параметры запроса
     * @return json ответ
     */
    public String getMoviesFromAPi(MovieRequestParams movieRequestParams) {

        String url = BASE_URL + PAGE + movieRequestParams.getPage() + LIMIT + movieRequestParams.getLimit() + TITLE + movieRequestParams.getTitle();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

    /**
     * Десериализация ответа от API кинопоиска
     * @param jsonString - json ответ
     * @return десериализованные данные ответа
     */
    public ResponseData<Movie> deserializeResponseData(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<ResponseData<Movie>>() {});
        } catch (JsonProcessingException e) {
            System.out.println("return null;" + e.getMessage());
            return new ResponseData<Movie>();
        }
    }

    /**
     * Сериализация фильмов для отправки клиенту
     * @param movies - фильмы
     * @return - json строка
     */
    public String serializeResponseData(List<Movie> movies) throws JsonProcessingException {
        // Создаем объект, который не может быть сериализован
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Intentional error");
            }
        };

        // Пробуем сериализовать этот объект, что вызовет JsonProcessingException
        return objectMapper.writeValueAsString(invalidObject);
      //  return objectMapper.writeValueAsString(movies);
    }

    /**
     * API кинопоиска иногда возвращает некорректные результаты,
     * возвращает поле, где название фильма пустое
     * причина - ошибочная запись в их базе данных
     * надо такое удалить
     */
    public List<Movie> clearMovie(List<Movie> movies) {
        // лямбда-выражение для удаления элементов из коллекции (Добавлена в java 8)
        movies.removeIf(temp -> temp.getName() == null || temp.getName().isEmpty());
        return movies;
    }



}

//TODO 401 Unauthorized: "{"message":"В запросе не указан токен!","error":"Unauthorized","statusCode":401}" обработка ответов
//TODO 400 Bad Request: "{"message":["Поле id должно быть числом или массивом чисел!","Значение поля id должно быть в диапазоне от 250 до 7000000!"],"error":"Bad Request","statusCode":400}"