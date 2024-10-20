package by.Danik.lab.config;

import by.Danik.lab.cache.SimpleCache;
import by.Danik.lab.interceptors.ClientInterceptor;
import by.Danik.lab.interceptors.RestTemplateInterceptor;
import by.Danik.lab.models.RequestCounter;
import by.Danik.lab.models.movie.entities.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private ClientInterceptor clientInterceptor;

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
    public SimpleCache<String, List<Movie>> movieSimpleCache() {
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

    /**
     * Счётчик подсчитывающий количество обращений к сервису фильмы
     * @return
     */
    @Bean
    public RequestCounter requestCounter() {
        return new RequestCounter();
    }

    /**
     * Управление потоками, создаём пул потоков размером 10
     * @return
     */
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    /**
     * Регистрируем свой интерцептор для работы с клиентскими заголовками
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clientInterceptor);
    }
}
