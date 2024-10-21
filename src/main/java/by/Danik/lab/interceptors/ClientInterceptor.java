package by.Danik.lab.interceptors;

import by.Danik.lab.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик клиентских запросов/ответов
 */
@Component
@Slf4j
public class ClientInterceptor implements HandlerInterceptor {

    // Вызывается перед тем как будет отдано контроллеру
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("------------------- Endpoint: " + request.getRequestURI() + " метод: " + request.getMethod() + "---------------------------");

        // Проверка и получение тела запроса для POST метода
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            if (request.getContentLength() == 0) {
                logger.error("POST запрос без тела");
                throw CustomException.builder().status(HttpStatus.BAD_REQUEST).message("POST запрос без тела").build();
            }
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // Получение параметров для GET запроса
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder paramsAsString = new StringBuilder();
            params.forEach((key, value) ->
                    paramsAsString.append(key).append(" = ").append(Arrays.toString(value)).append("; ")
            );
            logger.info("Параметры: " + paramsAsString);
        }

        return true;
    }

    //  Вызывается после выполнения контроллера, но до рендера представления.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    // Вызывается после завершения обработки запроса и рендера представления
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    logger.info("--------------------Запрос закончился------------------------");
    }

}