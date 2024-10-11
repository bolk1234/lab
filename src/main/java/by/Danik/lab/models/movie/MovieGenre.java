package by.Danik.lab.models.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Жанр фильма
 */
public class MovieGenre {
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
