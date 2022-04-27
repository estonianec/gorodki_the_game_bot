package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Cities;
import pro.sky.telegrambot.model.City;
import pro.sky.telegrambot.repository.CityRepository;
import pro.sky.telegrambot.repository.CountryRepository;
import pro.sky.telegrambot.repository.RegionRepository;
import pro.sky.telegrambot.service.CityService;

import java.util.HashSet;
import java.util.Set;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, RegionRepository regionRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean checkIsCityExistInDB(String city) {
        return cityRepository.getCitiesByNameEquals(city) != null;
    }

    @Override
    public Cities makeNewListOfCities(int count, Long chatId) {
        Set<City> newListOfCities = cityRepository.getRandomCities(count);
        Set<String> set = new HashSet<>();
        return new Cities(newListOfCities, set, chatId, "");
    }

    @Override
    public int getCountOfCitiesInDB() {
        return cityRepository.countOfCitiesFromDB();
    }

    @Override
    public String getRegionName(Long regionId) {
        return regionRepository.getRegionByRegionId(regionId).getName();
    }

    @Override
    public String getCountryName(Long countryId) {
        return countryRepository.getCountryByCountryId(countryId).getName();
    }

}
