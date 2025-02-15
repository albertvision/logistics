package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.dto.ShippingListDto;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.entity.ShippingStatus;
import bg.nbu.cscb532.logistics.data.entity.User;
import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;

import java.util.List;
import java.util.Optional;

public interface ShippingService {

    public Optional<Shipping> findById(Long id);

    public Shipping save(SaveShippingDto shippingDto);

    public Shipping createEntity(SaveShippingDto shippingDto);

    public List<Shipping> findAll(ShippingListDto shippingListDto);

    public Optional<ShippingStatus> getLastStatusType(Shipping shipping);

    public List<ShippingStatusType> getAllowedStatusType(Shipping shipping);

    public List<ShippingStatusType> getAllowedStatusType(SaveShippingDto shippingDto);

    public List<User> getAllowedSenders();
}
