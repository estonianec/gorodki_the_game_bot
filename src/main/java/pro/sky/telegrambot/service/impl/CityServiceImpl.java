package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Cities;
import pro.sky.telegrambot.repository.CityRepository;
import pro.sky.telegrambot.service.CityService;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public boolean checkIsCityExistInDB(String city) {
        return cityRepository.getCitiesByNameEquals(city.toLowerCase(Locale.ROOT)) != null;
    }

    @Override
    public Cities makeNewListOfCities(int count, Long chatId) {
        Set<String> newListOfCities = cityRepository.getRandomCities(count);
        Set<String> set = new HashSet<>();
        return new Cities(newListOfCities, set, chatId, "");
    }

    @Override
    public int getCountOfCitiesInDB() {
        return cityRepository.countOfCitiesFromDB();
    }

}
