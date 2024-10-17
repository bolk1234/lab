package by.Danik.lab.repositories;

import by.Danik.lab.models.movie.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Страны
 * имеет связь многие ко многим к фильмам
 * Значения в таблице не должны дублироваться
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);
}
