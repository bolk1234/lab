package by.Danik.lab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Получить данные о фильмах из API кинопоиска
 */
@Service
public class MovieServices {
    private final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/search"; // API кинопоиска
    @Value("${kinopoisk.token}")
    private String token;                                                           // токен выданный кинопоиском
    private final RestTemplate restTemplate;

    public MovieServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param title - название фильма
     * @param page -  количество запрошеных страниц
     * @param limit - количество элементов на странице
     * @return TODO
     */
    public String getMovieByTitle(String title, int page, int limit) {

        // параметры гет запроса
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("query", title)
                .toUriString();

        // заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("X-API-KEY", token);

        // заголовок + тело запроса
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Отправка GET запроса
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // тело ответа
        String responseBody = response.getBody();
        System.out.println(responseBody);


        // заголовки ответа
        HttpHeaders headers1 = response.getHeaders();

    /*    headers1.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
*/


        return responseBody;
    }
}

//TODO 401 Unauthorized: "{"message":"В запросе не указан токен!","error":"Unauthorized","statusCode":401}" обработка ответов