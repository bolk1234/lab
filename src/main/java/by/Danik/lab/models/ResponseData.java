package by.Danik.lab.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Десериализованный ответ от API кинопоиска
 * подходит для запросов где может вернуться несколько объектов
 * надо сверяться с документацией
 * @param <T> - класс полученных объектов
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class ResponseData<T> {
    private List<T> docs;           // объекты пришедшие в ответе
    private int total;              // всего совпадений в базе кинопоиска
    private int limit;              // сколько фильмов пришло в ответе
    private int page;               // текущая страница (для постраничной навигации)
    private int pages;              // всего страниц (для постраничной навигации)
}
