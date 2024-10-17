package by.Danik.lab;

import by.Danik.lab.interceptors.HeaderInterceptor;
import by.Danik.lab.interceptors.RestTemplateInterceptor;
import by.Danik.lab.models.movie.MovieRequestParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

     @Value("${kinopoisk.token}")
    private String token;            // токен выданный кинопоиском

    /**
     * Взаимодействие с REST сервисами
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        // интерсептор
        interceptors.add(new RestTemplateInterceptor(token));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    /**
     * Кеш для поиска фильма по имени
     * @return
     */
    @Bean
    public SimpleCache<MovieRequestParams, String> movieRequestParamsCache() {
        return new SimpleCache<>();
    }

    /**
     * Сериализация/десериализация
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
