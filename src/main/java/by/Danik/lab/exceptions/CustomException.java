package by.Danik.lab.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Исключения выбрасываемые мной
 * паттерн строитель
 * удобно если появятся новые поля или если не хочется использовать все поля
 */
@Getter
@Builder
@ToString
public class CustomException extends RuntimeException {
    private final String message; // сообщение об ошибке
    private final HttpStatus status;     // статус ответа
}
