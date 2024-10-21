package by.Danik.lab.services;

import by.Danik.lab.cache.SimpleCache;
import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.models.movie.entities.Movie;
import by.Danik.lab.repositories.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Тест для сервиса поиска фильма по названию
 */
@ExtendWith(value = {MockitoExtension.class})
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private SimpleCache<String, List<Movie>> movieSimpleCache;

    @Mock
    private MovieRepository movieRepository;

    private List<Movie> mockMovies;

    @BeforeEach
    public void setUp() {
        mockMovies = Arrays.asList(new Movie(), new Movie());
    }

    @Test
    public void testMethodGet_ShouldReturnMovies() {
        // Настройка mock для кеша
        when(movieSimpleCache.containsKey("Inception")).thenReturn(false);

        // Настройка mock для restTemplate, если это требуется
        // when(restTemplate.getForObject(...)).thenReturn(...);

        // Настройка входных данных
        MovieRequestParams params = new MovieRequestParams("Inception");

        // Вызов метода для тестирования
        List<Movie> result = movieService.methodGet(params);

        // Проверка результата
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getName());

        verify(movieSimpleCache).containsKey("Inception");
        // Проверьте, что вызов к RestTemplate произошел, если необходимо
    }
}