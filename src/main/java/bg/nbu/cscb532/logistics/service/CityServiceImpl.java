package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.City;
import bg.nbu.cscb532.logistics.data.entity.City_;
import bg.nbu.cscb532.logistics.data.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    public List<City> findAll() {
        return cityRepository.findAll(Sort.by(City_.POSTAL_CODE));
    }



    @EventListener
    void seedCities(ContextRefreshedEvent event) {
        List<City> cities = List.of(
                City.builder().name("Sofia")
                        .postalCode(1000)
                        .addressDeliveries(true)
                        .build(),
                City.builder().name("Plovdiv")
                        .postalCode(4000)
                        .addressDeliveries(true)
                        .build(),
                City.builder().name("Varna")
                        .postalCode(9000)w
                        .addressDeliveries(true)
                        .build(),
                City.builder().name("Burgas")
                        .postalCode(8000)
                        .addressDeliveries(true)
                        .build(),
                City.builder().name("Stara Zagora")
                        .postalCode(6000)
                        .addressDeliveries(false)
                        .build()
        );

        // todo reuse create method
        cities.forEach(it -> {
            try {
                cityRepository.save(it);
            } catch (DataIntegrityViolationException ignore) {

            }
        });
    }
}
