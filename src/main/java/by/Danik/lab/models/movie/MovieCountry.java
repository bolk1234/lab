package by.Danik.lab.models.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Страна, в которой снимался фильм
 */
@Setter
@Getter
public class MovieCountry {
    private String name;
}