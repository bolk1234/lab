package by.Danik.lab.interceptors;

import by.Danik.lab.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


import java.io.IOException;

/**
 * Интерцептор для перехвата заголовков restTemplate
 * запросы отправляемые на API кинопоиска
 */
@Slf4j
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    String token;

    public RestTemplateInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("accept", "application/json");
        request.getHeaders().set("X-API-KEY", token);

        // перехватываем ответ
        ClientHttpResponse response =  execution.execute(request, body);

        // Проверяем код ответа, если код больше чем 500 значит ошибка на сервера на стороне API кинопоиска
        if (response.getStatusCode().value() >= 500) {
            logger.error("API кинопоиска не работает");

            throw CustomException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("API кинопоиска не работает")
                    .build();
        }

        return response;
    }
}

