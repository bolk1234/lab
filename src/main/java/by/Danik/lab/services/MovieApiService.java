package by.Danik.lab.services;

import by.Danik.lab.exceptions.CustomException;
import by.Danik.lab.models.ResponseDataFromApi;
import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.models.movie.entities.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Подключение к АПИ кинопоиска для поиска фильма по имени
 */
@Slf4j
@Service
public class MovieApiService {
    private final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/search?";     // API кинопоиска
    private final String PAGE = "&page=";     // ответ может быть на несколько страниц, номер текущей страницы
    private final String LIMIT = "&limit=";     // сколько элементов на страницу, значение 1 значит забрать только один элемент
    private final String TITLE = "&query=";     // название фильма


    @Autowired
    private ObjectMapper objectMapper;                                   // сериализация/десериализация (класс из библиотеки Jackson)

    @Autowired
    private RestTemplate restTemplate;                                    // клиент для HTTP запросов

    /**
     * Запрос к API кинопоиска
     * @param movieRequestParams - параметры запроса, которые пришли от клиента
     * @return json ответ
     */
    public String restTemplateGet(MovieRequestParams movieRequestParams) {

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
    public ResponseDataFromApi<Movie> deserializeResponseData(String jsonString) {
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

    /** Не то чтобы и нужен
     * Сериализация фильмов для отправки клиенту
     * @param movies - фильмы
     * @return - json строка

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
     */
}
