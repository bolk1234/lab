package by.Danik.lab.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Обработчик всех исключений
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Валидация входных параметров в контроллерах
     * @param ex - объект исключения
     * @return - заголовок и ответ ответа
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<String> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {

        // собираем все ошибки валидации
        List<String> errorMessages = new ArrayList<>();
        ex.getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
        logger.warn("Валидация не пройдена, причина: " + errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Исключения выброшенные в блоках catch
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleMovieNotFoundException(CustomException ex) {
        // логирование там где исключение выбрасывается
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    /**
     * Для всех не пойманных исключений
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("ВНИМАНИЕ: не перехваченное исключение: имя класса " + ex.getClass() + " Причина = " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }


}
