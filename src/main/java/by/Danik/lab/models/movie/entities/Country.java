package by.Danik.lab.models.movie.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Страна, в которой снимался фильм
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "Countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}