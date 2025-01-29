package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    public Optional<City> findById(Long id);
    public List<City> findAll();
}
