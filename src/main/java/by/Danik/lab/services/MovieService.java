package by.Danik.lab.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Получить данные о фильмах из API кинопоиска
 */
@Service
public class MovieService {
    private final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/search?"; // API кинопоиска
    private final String PAGE = "&page=";     // ответ может быть на несколько страниц, номер текущей страницы
    private final String LIMIT = "&limit=";     // сколько элементов на страницу, значение 1 значит забрать только один элемент
    private final String TITLE = "&query=";     // сколько элементов на страницу, значение 1 значит забрать только один элемент

    private final RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param title - название фильма
     * @param page -  количество запрошенных страниц
     * @param limit - количество элементов на странице
     * @return TODO
     */
    public String getMovieByTitle(String title, int page, int limit) {

     String url = BASE_URL + PAGE + page + LIMIT + limit + TITLE + title;
    System.out.println(url);

        // Отправка GET запроса
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // тело ответа
        String responseBody = response.getBody();

        // заголовки ответа
        HttpHeaders headers1 = response.getHeaders();

    /*    headers1.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
*/
        System.out.println(responseBody);

        return responseBody;
    }
}

//TODO 401 Unauthorized: "{"message":"В запросе не указан токен!","error":"Unauthorized","statusCode":401}" обработка ответов
//TODO 400 Bad Request: "{"message":["Поле id должно быть числом или массивом чисел!","Значение поля id должно быть в диапазоне от 250 до 7000000!"],"error":"Bad Request","statusCode":400}"