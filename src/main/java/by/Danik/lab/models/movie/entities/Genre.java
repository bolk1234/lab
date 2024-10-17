package by.Danik.lab.models.movie.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Жанр фильма
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "genres")
public class Genre {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
}
