package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.Cities;

import java.util.List;

public interface CityService {

    boolean checkIsCityExistInDB(String city);

    Cities makeNewListOfCities(int count, Long chatId);

    int getCountOfCitiesInDB();

    String getRegionName(Long regionId);

    String getCountryName(Long countryId);
}
