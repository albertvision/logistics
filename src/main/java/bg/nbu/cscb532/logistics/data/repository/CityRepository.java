package bg.nbu.cscb532.logistics.data.repository;

import bg.nbu.cscb532.logistics.data.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
