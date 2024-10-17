package by.Danik.lab.models.movie.entities;

import by.Danik.lab.models.movie.entities.Country;
import by.Danik.lab.models.movie.entities.Genre;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Фильм
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long localId;                        //primary key, так как id приходит из АПИ кинопоиска, нужно изменить название своего первичного ключа

    @JsonProperty("id")
    private int kinoPoiskId;                    // id фильма в базе кинопоиска
    private String name;                        // название
    @JsonProperty("year")
    private int releaseYear;                    // год выпуска
    @Lob
    private String description;                 // синопсис
    private int typeNumber;                     //  тип контента (1: Фильм, 2: Сериал, 3: Шоу, 4: Мини-сериал, 5: Видео)
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime creationRecord ;            // время создания записи

    @ManyToMany(cascade = CascadeType.ALL)                      // Связь многие-ко-многим с жанрами
    @JoinTable(
        name = "movie_genre",                                   // Имя промежуточной таблицы
        joinColumns = @JoinColumn(name = "movie_id"),           // Внешний ключ для movies
        inverseJoinColumns = @JoinColumn(name = "genre_id")     // Внешний ключ для genres
    )
    private List<Genre> genres;                                 // жанры

    @ManyToMany(cascade = CascadeType.ALL)                      // Связь многие-ко-многим со странами
    @JoinTable(
        name = "movie_country",                                 // Имя промежуточной таблицы
        joinColumns = @JoinColumn(name = "movie_id"),           // Внешний ключ для movies
        inverseJoinColumns = @JoinColumn(name = "country_id")   // Внешний ключ для countries
    )
    private List<Country> countries;                       // страны
}

