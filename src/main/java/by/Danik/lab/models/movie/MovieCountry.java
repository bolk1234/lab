package by.Danik.lab.models.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Страна, в которой снимался фильм
 */
public class MovieCountry {
    private String name;

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}