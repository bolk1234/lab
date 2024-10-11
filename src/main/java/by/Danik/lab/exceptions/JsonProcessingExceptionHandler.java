package by.Danik.lab.exceptions;

/**
 * Ошибка сериализации/десериализации
 */
public class JsonProcessingExceptionHandler extends AbstractExceptionHandler {
    public JsonProcessingExceptionHandler(String message, int status) {
        super(message, status);
    }
}
