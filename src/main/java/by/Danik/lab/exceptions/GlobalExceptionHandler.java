package by.Danik.lab.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    /**
     * Исключения выброшенные в блоках catch
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleMovieNotFoundException(CustomException ex) {
        //TODO логирование
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Запрос на не существующий ресурс
     * @param ex
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Запрашиваемый ресурс не найден");
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
