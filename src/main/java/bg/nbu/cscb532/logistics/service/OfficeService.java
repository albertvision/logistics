package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveOfficeDto;
import bg.nbu.cscb532.logistics.data.entity.Office;

import java.util.List;
import java.util.Optional;

public interface OfficeService {
    public List<Office> findAll();

    public Optional<Office> findById(Long id);

    public Office save(SaveOfficeDto saveOfficeDto);
}
