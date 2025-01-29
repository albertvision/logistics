package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.dto.SaveOfficeDto;

import java.util.List;
import java.util.Optional;

public interface OfficeService {
    public List<Office> getAll();

    public Optional<Office> getById(Long id);

    public Office save(SaveOfficeDto saveOfficeDto);
}
