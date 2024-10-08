package by.Danik.lab.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Movie {
    private int id;
    private String name;
    private String alternativeName;
    private String enName;
    private String type;
    private int year;
    private String description;
    private String shortDescription;
    private int movieLength;
    private List<Name> names;
    private ExternalId externalId;
    private Logo logo;
    private Poster poster;
    private Backdrop backdrop;
    private Rating rating;
    private Votes votes;
    private List<Genre> genres;
    private List<Country> countries;
    private List<ReleaseYear> releaseYears;
    private boolean isSeries;
    private boolean ticketsOnSale;
    private int totalSeriesLength;
    private int seriesLength;
    private String ratingMpaa;
    private int ageRating;
    private int top10;
    private int top250;
    private int typeNumber;
    private String status;

    // Getters and setters

    public static class Name {
        private String name;
        private String language;
        private String type;

        // Getters and setters
    }

    public static class ExternalId {
        @JsonProperty("kpHD")
        private String kpHD;
        private String imdb;
        private int tmdb;

        // Getters and setters
    }

    public static class Logo {
        private String url;

        // Getters and setters
    }

    public static class Poster {
        private String url;
        private String previewUrl;

        // Getters and setters
    }

    public static class Backdrop {
        private String url;
        private String previewUrl;

        // Getters and setters
    }

    public static class Rating {
        private double kp;
        private double imdb;
        private double tmdb;
        private double filmCritics;
        private double russianFilmCritics;
        private double await;

        // Getters and setters
    }

    public static class Votes {
        private String kp;
        private int imdb;
        private int tmdb;
        private int filmCritics;
        private int russianFilmCritics;
        private int await;

        // Getters and setters
    }

    public static class Genre {
        private String name;

        // Getters and setters
    }

    public static class Country {
        private String name;

        // Getters and setters
    }

    public static class ReleaseYear {
        private int start;
        private int end;

        // Getters and setters
    }

    @Override
    public String toString() {

        return "String " + name;
    }
}

