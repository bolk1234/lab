package by.Danik.lab.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обработчик клиентских запросов/ответов
 */
@Component
@Slf4j
public class ClientInterceptor implements HandlerInterceptor {

    // Вызывается перед тем как будет отдано контроллеру
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

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