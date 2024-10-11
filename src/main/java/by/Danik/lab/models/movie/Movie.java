package by.Danik.lab.models.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Фильм
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    private int id;                     // id фильма в базе кинопоиска
    private String name;                // название
    private int year;                   // год выпуска
    private String description;         // синопсис
    private int typeNumber;              //  тип контента (1: Фильм, 2: Сериал, 3: Шоу, 4: Мини-сериал, 5: Видео)
    private List<MovieGenre> genres;        // жанры
    private List<MovieCountry> countries;        // страны
}

