package by.Danik.lab.controllers;

import by.Danik.lab.models.movie.MovieRequestParams;
import by.Danik.lab.models.movie.entities.Movie;
import by.Danik.lab.services.CounterService;
import by.Danik.lab.services.MovieService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для счётчика запросов
 */
@Slf4j
@RestController
public class CounterController {
    @Autowired
    private CounterService counterService;

    @GetMapping("/counter")
    public int getMovies() {
        return counterService.getCurrentCount();
    }
}
