package by.Danik.lab.controllers;

import by.Danik.lab.service.MovieServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    private final MovieServices movieService;

    public MovieController(MovieServices movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String getMovies(@RequestParam(value="title") String title,
                            @RequestParam(value="page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "1") int limit) {
        System.out.println("title = " + title + page + limit);
        return movieService.getMovieByTitle(title, page, limit);
    }
}

