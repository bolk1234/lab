package by.Danik.lab.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieApiServiceTest {
    @InjectMocks
    private MovieApiService movieApiService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;


    @Test
    public void testRestTemplateGet_ShouldReturnValidResponse() throws Exception {
        // Подготовка ожидаемого ответа от API
        String expectedUrl = "https://api.kinopoisk.dev/v1.4/movie/search?page=1&limit=1&query=Inception";
        String mockResponse = "{\"films\": [{\"id\": 1, \"title\": \"Inception\"}]}"; // Пример ответа

        // Подготовка мока для RestTemplate
        when(restTemplate.getForEntity(expectedUrl, String.class)).thenReturn(ResponseEntity.ok(mockResponse));


        // Проверка вызова правильного URL
        verify(restTemplate).getForEntity(expectedUrl, String.class);
    }
}
