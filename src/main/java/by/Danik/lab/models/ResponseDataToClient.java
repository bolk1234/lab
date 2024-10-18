package by.Danik.lab.models;

import by.Danik.lab.models.movie.entities.Movie;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Подготовленный ответ для клиента
 * главная функция: собрать в сервисе статус ответа + тело ответа и передать это контроллеру
 * если будут нужны заголовки, их можно добавить в инцесепторе
 */
@Getter
@Setter
@ToString
public class ResponseDataToClient {
    List<Movie> body;           // тело ответа (объекты фильмов)
    HttpStatus httpStatus;      // статус ответа

    public ResponseDataToClient(List<Movie> body, HttpStatus httpStatus) {
        this.body = body;
        this.httpStatus = httpStatus;
    }
}
