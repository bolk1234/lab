package by.Danik.lab.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * По запросу клиента ничего не найдено
 */
public class NotFoundException extends AbstractExceptionHandler {

    public NotFoundException(String message, int status) {
        super(message, status);
    }
}
