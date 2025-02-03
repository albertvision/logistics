package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveServiceDto;
import bg.nbu.cscb532.logistics.data.entity.Service;
import bg.nbu.cscb532.logistics.data.entity.Service_;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import bg.nbu.cscb532.logistics.data.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static bg.nbu.cscb532.logistics.data.spec.ServiceSpec.*;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Override
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll(
            Sort.by(
                Sort.Order.asc(Service_.SERVICE_TYPE),
                Sort.Order.asc(Service_.MIN_WEIGHT)
            )
        );
    }

    @Override
    public Optional<Service> getForUnitValueAndType(Integer unitValue, ServiceType type) {
        return serviceRepository.findBy(
            minWeightLessOrEqualTo(unitValue).and(typeIs(type)),
            q -> q.sortBy(sortByMinWeightDesc()).first()
        );
    }

    public BigDecimal getAmountEur(Shipping shipping) {
        return calculateServices(shipping)
            .values()
            .stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<Service, BigDecimal> calculateServices(Shipping shipping) {
        return getServices(shipping)
            .stream()
            .collect(Collectors.toMap(it -> it, it -> getAmountEur(shipping.getWeightInGrams(), it)));
    }

    public List<Service> getServices(Shipping shipping) {
        return getServiceTypes(shipping)
            .stream()
            .map(it -> getForUnitValueAndType(shipping.getWeightInGrams(), it))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Service save(SaveServiceDto saveServiceDto) {
        Service service = saveServiceDto.getId() == null
            ? Service.builder().build()
            : serviceRepository.findById(saveServiceDto.getId()).orElseThrow(() -> new IllegalArgumentException("Service not found"));

        service.setServiceType(saveServiceDto.getServiceType());
        service.setMinWeight(saveServiceDto.getMinWeight());
        service.setBasePriceEur(saveServiceDto.getBasePriceEur());
        service.setPriceEurPerWeight(saveServiceDto.getPriceEurPerWeight());

        try {
            return serviceRepository.save(service);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Service for that min weight already exists");
        }
    }

    private static List<ServiceType> getServiceTypes(Shipping shipping) {
        List<ServiceType> serviceTypes = new java.util.LinkedList<>();

        serviceTypes.add(ServiceType.TRANSPORTATION);
        serviceTypes.add(
            shipping.getSenderOffice() == null
                ? ServiceType.COLLECTION_FROM_ADDRESS
                : ServiceType.COLLECTION_FROM_OFFICE
        );
        serviceTypes.add(
            shipping.getReceiverOffice() == null
                ? ServiceType.DELIVERY_TO_ADDRESS
                : ServiceType.DELIVERY_TO_OFFICE
        );

        return serviceTypes;
    }

    private static BigDecimal getAmountEur(Integer unitValue, Service it) {
        return it.getBasePriceEur().add(
            BigDecimal.valueOf(unitValue)
                .multiply(it.getPriceEurPerWeight())
        );
    }

    @EventListener
    void seedServices(ContextRefreshedEvent event) {
        List<Service> services = List.of(
            Service.builder()
                .serviceType(ServiceType.TRANSPORTATION)
                .minWeight(0)
                .basePriceEur(BigDecimal.valueOf(4.53))
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.COLLECTION_FROM_OFFICE)
                .minWeight(0)
                .basePriceEur(BigDecimal.ZERO)
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.COLLECTION_FROM_ADDRESS)
                .minWeight(0)
                .basePriceEur(BigDecimal.valueOf(1.54))
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.DELIVERY_TO_OFFICE)
                .minWeight(0)
                .basePriceEur(BigDecimal.ZERO)
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.DELIVERY_TO_ADDRESS)
                .minWeight(0)
                .basePriceEur(BigDecimal.valueOf(4.4))
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            //
            Service.builder()
                .serviceType(ServiceType.TRANSPORTATION)
                .minWeight(3000)
                .basePriceEur(BigDecimal.valueOf(6.37))
                .priceEurPerWeight(BigDecimal.valueOf(0.0006))
                .build(),
            Service.builder()
                .serviceType(ServiceType.COLLECTION_FROM_OFFICE)
                .minWeight(3000)
                .basePriceEur(BigDecimal.ZERO)
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.COLLECTION_FROM_ADDRESS)
                .minWeight(3000)
                .basePriceEur(BigDecimal.valueOf(4.4))
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.DELIVERY_TO_OFFICE)
                .minWeight(3000)
                .basePriceEur(BigDecimal.ZERO)
                .priceEurPerWeight(BigDecimal.ZERO)
                .build(),
            Service.builder()
                .serviceType(ServiceType.DELIVERY_TO_ADDRESS)
                .minWeight(3000)
                .basePriceEur(BigDecimal.valueOf(7.3))
                .priceEurPerWeight(BigDecimal.ZERO)
                .build()
        );

        // todo reuse create method
        services.forEach(it -> {
            try {
                serviceRepository.save(it);
            } catch (DataIntegrityViolationException ignore) {
                //
            }
        });
    }
}
