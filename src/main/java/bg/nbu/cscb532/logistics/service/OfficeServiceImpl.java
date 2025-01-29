package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.City;
import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.data.repository.OfficeRepository;
import bg.nbu.cscb532.logistics.dto.SaveOfficeDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final CityServiceImpl cityServiceImpl;

    public OfficeServiceImpl(OfficeRepository officeRepository, CityServiceImpl cityServiceImpl) {
        this.officeRepository = officeRepository;
        this.cityServiceImpl = cityServiceImpl;
    }

    @Override
    public List<Office> getAll() {
        return officeRepository.findAll();
    }

    @Override
    public Optional<Office> getById(Long id) {
        return officeRepository.findById(id);
    }

    @Override
    public Office save(SaveOfficeDto saveOfficeDto) {
        City city = cityServiceImpl.findById(saveOfficeDto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        Office office;
        if (saveOfficeDto.getId() == null) {
            office = new Office();
        } else {
            office = officeRepository.findById(saveOfficeDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Office not found"));
        }

        office.setName(saveOfficeDto.getName());
        office.setAddress(saveOfficeDto.getAddress());
        office.setCity(city);

        return officeRepository.save(office);
    }
}
