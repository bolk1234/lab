package by.Danik.lab.models.movie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Параметры запроса на сервер для поиска фильма по названию
 */
@Getter
@EqualsAndHashCode
@ToString
public class MovieRequestParams {
    private final String title;                                                //название фильма
    private final int page;                                                    // номер страницы (для постраничной навигации)
    private final int limit;                                                   // сколько вернуть фильмов в запросе

    public MovieRequestParams(String title, int page, int limit) {
        this.title = title;
        this.page = page;
        this.limit = limit;
    }

    public MovieRequestParams(String title) {
        this.title = title;
        this.page = 1;
        this.limit = 1;
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