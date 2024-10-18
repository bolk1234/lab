package by.Danik.lab.controllers;

import by.Danik.lab.models.ResponseDataToClient;
import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Всё запросы связанные с фильмами
 */
@Slf4j
@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;

    /**
     * Поиск фильма по названию, так как метод GET и есть желание проводить валидацию через аннотации, приходится прописывать условия в параметрах функции
     * @param title - название фильма, не может быть пустым, не можут содержать специальные символы ()'\-:.,?! (во всяком случае теоретически)
     * @param page - текущая страница (при постраничной навигации), не может быть отрицательной
     * @param limit - количество фильмов, положительное число не больше 1000 (кинопоиск не отдаёт больше 1000 за один раз)
     * @return
     */
    @GetMapping("/movies")
    public ResponseEntity<String> getMovies(
            @RequestParam(value="title") @NotEmpty(message = "Название фильма не может быть пустым") @Pattern(regexp = "^[A-Za-zА-Яа-я0-9\\s]+$", message = "Название фильма недопустимо") String title,
            @RequestParam(value="page", defaultValue = "1") @Positive(message = "Номер страницы может быть только положительным") int page,
            @RequestParam(value = "limit", defaultValue = "1") @Positive(message = "Вы действительно желаете получить меньше одного фильма") @Max(value = 1000, message = "Кинопоиск не поддерживает больше 1000 значений на раз") int limit) {
        MovieRequestParams movieRequestParams = new MovieRequestParams(title, page, limit);

        return ResponseEntity.ok(movieService.methodGet(movieRequestParams));
    }

    /**
     *
     * @param titles
     * @return
     */
    @PostMapping("/movies/bulk")
    public ResponseEntity<String> getMoviesBulk(@RequestBody List<String> titles) {
        // статус ответа 200 ОК, раз сюда дошли значит всё хорошо
        return ResponseEntity.ok(movieService.methodPostBulk(titles));
    }
}



/**
 * ^ — начало строки.
 * A-Za-z — любые латинские буквы (верхний и нижний регистр).
 * А-Яа-я — любые кириллические буквы (верхний и нижний регистр).
 * 0-9 — любые цифры.
 * \s — пробелы.
 * \W — любые небуквенные символы (например, (), ', -, :, ., !, ?).
 * \d — любая цифра (0-9).
 * + — один или более раз.
 * $ — конец строки.
 */
