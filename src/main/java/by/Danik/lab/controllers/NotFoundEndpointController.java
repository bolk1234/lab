package by.Danik.lab.controllers;


import by.Danik.lab.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Обработка всех не существующих ендпоинтов
 */

@Slf4j
@RestController
public class NotFoundEndpointController {

    @RequestMapping(value = "/**")
    public void handleNotFoundEndpoint(HttpServletRequest request) {
        logger.warn("Был запрошен не существующий endpoint: " +  request.getRequestURI() + " параметры: " + request.getQueryString() + " метод: " + request.getMethod());

        throw CustomException.builder().
                status(HttpStatus.NOT_FOUND)
                .message("Запрашиваемый ресурс не найден")
                .build();
    }
}
