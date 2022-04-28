package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country getCountryByCountryId(long countryId);
}
