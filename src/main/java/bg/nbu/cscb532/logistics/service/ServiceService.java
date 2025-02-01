package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.Service;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServiceService {
    public Optional<Service> getForUnitValueAndType(Integer unitValue, ServiceType type);

    public BigDecimal getAmountEur(Shipping shipping);

    public Map<Service, BigDecimal> calculateServices(Shipping shipping);

    public List<Service> getServices(Shipping shipping);
}
