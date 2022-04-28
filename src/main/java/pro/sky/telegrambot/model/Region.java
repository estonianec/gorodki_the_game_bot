package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Region {
    @Id
    long regionId;
    long countryId;

    public Region() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return regionId == region.regionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionId);
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
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

    public Region(long regionId, long countryId, String name) {
        this.regionId = regionId;
        this.countryId = countryId;
        this.name = name;
    }

    String name;
}
