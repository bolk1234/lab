package by.Danik.lab.repositories;

import by.Danik.lab.models.movie.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Жанры
 * имеет связь многие ко многим к фильмам
 * Значения в таблице не должны дублироваться
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
