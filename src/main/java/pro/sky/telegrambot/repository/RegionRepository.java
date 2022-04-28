package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Region getRegionByRegionId(long regionId);
}
