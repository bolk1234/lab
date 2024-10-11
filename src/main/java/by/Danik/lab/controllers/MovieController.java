package by.Danik.lab.controllers;

import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Valid
    @GetMapping("/movies")
    public String getMovies(@RequestParam(value="title") @NotEmpty String title,
                            @RequestParam(value="page", defaultValue = "1") @Min(1) int page,
                            @RequestParam(value = "limit", defaultValue = "5") @Min(1) int limit) throws JsonProcessingException {

        MovieRequestParams movieRequestParams = new MovieRequestParams(title, page, limit);
        return movieService.getMovieByTitle(movieRequestParams);
    }
}

