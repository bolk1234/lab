package by.Danik.lab.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

// интерсептор заголовков
public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    String token;

    public HeaderInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("accept", "application/json");
        request.getHeaders().set("X-API-KEY", token);
        System.out.println(token);
        return execution.execute(request, body);
    }
}
