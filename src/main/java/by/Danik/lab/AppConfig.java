package by.Danik.lab;

import by.Danik.lab.interceptors.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Value("${kinopoisk.token}")
    private String token;            // токен выданный кинопоиском

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // создаём лист на случай если понадобятся ещё интерсепторы
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        // интерсепторы заголовков
        interceptors.add(new HeaderInterceptor(token));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
