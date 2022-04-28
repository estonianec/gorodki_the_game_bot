package pro.sky.telegrambot.model;

import java.util.Objects;
import java.util.Set;

public class Cities {
    Set<City> listOfCities;
    Set<String> listOfUsedCities;
    Long chatId;
    String lastCity;

    public Cities(Set<City> listOfCities, Set<String> listOfUsedCities, Long chatId, String lastCity) {
        this.listOfCities = listOfCities;
        this.listOfUsedCities = listOfUsedCities;
        this.chatId = chatId;
        this.lastCity = lastCity;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "listOfCities=" + listOfCities +
                ", listOfUsedCities=" + listOfUsedCities +
                ", chatId=" + chatId +
                ", lastCity='" + lastCity + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cities cities = (Cities) o;
        return Objects.equals(listOfCities, cities.listOfCities) && Objects.equals(listOfUsedCities, cities.listOfUsedCities) && Objects.equals(chatId, cities.chatId) && Objects.equals(lastCity, cities.lastCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listOfCities, listOfUsedCities, chatId, lastCity);
    }

    public void addNewCityToUsedList(String newUsedCity) {
        this.listOfUsedCities.add(newUsedCity);
    }

    public Set<City> getListOfCities() {
        return listOfCities;
    }

    public void setListOfCities(Set<City> listOfCities) {
        this.listOfCities = listOfCities;
    }

    public Set<String> getListOfUsedCities() {
        return listOfUsedCities;
    }

    public void setListOfUsedCities(Set<String> listOfUsedCities) {
        this.listOfUsedCities = listOfUsedCities;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getLastCity() {
        return lastCity;
    }

    public void setLastCity(String lastCity) {
        this.lastCity = lastCity;
    }
}
