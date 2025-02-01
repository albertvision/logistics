package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.entity.ShippingStatus;

import java.util.List;
import java.util.Optional;

public interface ShippingService {
    public Shipping save(SaveShippingDto shippingDto);

    public Shipping createEntity(SaveShippingDto shippingDto);

    public List<Shipping> findAll();

    public Optional<ShippingStatus> getLastStatusType(Shipping shipping);
}
