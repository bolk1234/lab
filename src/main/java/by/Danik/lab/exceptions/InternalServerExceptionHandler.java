package by.Danik.lab.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerExceptionHandler extends AbstractExceptionHandler {

    public InternalServerExceptionHandler(String message, int status) {
        super(message, status);
    }
}
