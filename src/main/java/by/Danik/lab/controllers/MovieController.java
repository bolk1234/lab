package by.Danik.lab.controllers;

import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String getMovies(
            @RequestParam(value="title") @NotEmpty(message = "Название фильма не может быть пустым") @Pattern(regexp = "^[A-Za-zА-Яа-я0-9\\s]+$", message = "Название фильма недопустимо") String title,
            @RequestParam(value="page", defaultValue = "1") @Positive(message = "Номер страницы может быть только положительным") int page,
            @RequestParam(value = "limit", defaultValue = "1") @Positive(message = "Вы действительно желаете получить меньше одного фильма") @Max(value = 1000, message = "Кинопоиск не поддерживает больше 1000 значений на раз") int limit) {
        MovieRequestParams movieRequestParams = new MovieRequestParams(title, page, limit);

        logger.info("------------------------Пришёл GET запрос----------------------------");

        return movieService.getMovieByTitle(movieRequestParams);
    }

    /**
     *
     * @param titles
     * @return
     */
    @PostMapping("/movies/bulk")
    public String getMoviesBulk(@RequestBody List<String> titles) {

        logger.info("------------------------Пришёл bulk запрос----------------------------");

        // stream - превращает в поток данных
        // map - один из методов stream, принимает параметр лямбда-выражение, создаёт новый поток, не изменяя исходный
        // toList() - собирает всё что вернёт map в ArrayList
        return titles.stream()
                //лямбда выражение, для каждого элемента списка вызвать метод getMoviesBulk и передать параметры запроса
                .map(title -> movieService.getMoviesBulk(new MovieRequestParams(title)))
                .toList().toString();
    }


}

