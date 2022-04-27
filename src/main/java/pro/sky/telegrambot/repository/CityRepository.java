package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.City;

import java.util.Set;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "SELECT name FROM city ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    Set<String> getRandomCities(int count);

    City getCitiesByNameEquals(String city);

    @Query(value = "select count(*) from city", nativeQuery = true)
    int countOfCitiesFromDB();
}
