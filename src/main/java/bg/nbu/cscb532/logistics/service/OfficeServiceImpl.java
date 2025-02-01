package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveOfficeDto;
import bg.nbu.cscb532.logistics.data.entity.Address;
import bg.nbu.cscb532.logistics.data.entity.City;
import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.data.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final CityServiceImpl cityServiceImpl;

    @Override
    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    @Override
    public Optional<Office> findById(Long id) {
        return officeRepository.findById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
        office.setAddress(new Address(saveOfficeDto.getAddress(), city));

        return officeRepository.save(office);
    }
}
