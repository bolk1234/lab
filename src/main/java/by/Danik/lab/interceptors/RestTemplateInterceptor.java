package by.Danik.lab.interceptors;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Интерцептор для перехвата заголовков restTemplate
 * запросы отправляемые на API кинопоиска
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    String token;

    public RestTemplateInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("accept", "application/json");
        request.getHeaders().set("X-API-KEY", token);
        return execution.execute(request, body);
    }
}
