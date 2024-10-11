package by.Danik.lab.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * Обработчик исключений
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Валидация входных параметров в контроллерах
     * Прикручена к аннотациям (в старых версиях спринг вызывается другой, более информативный класс)
     * @param ex - объект исключения
     * @return - заголовок и ответ ответа
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<String> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка валидации, проверьте значения");
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        //TODO логирование
        System.out.println("jib,rf сериалзации" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }


    @ExceptionHandler({NotFoundException.class, InternalServerExceptionHandler.class, JsonProcessingExceptionHandler.class})
    public ResponseEntity<String> handleMovieNotFoundException(NotFoundException ex) {
        //TODO логирование
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Для всех не пойманных исключений
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("глобальное мля: " + ex.getClass().toString() + " Причина = " + ex.getMessage());
    }


}
