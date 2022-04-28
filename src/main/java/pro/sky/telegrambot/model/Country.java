package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Country {
    @Id
    long countryId;
    String name;

    public Country() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return countryId == country.countryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId);
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country(long countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }
}
