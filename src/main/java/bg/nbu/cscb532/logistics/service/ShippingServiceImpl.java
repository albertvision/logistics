package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.entity.*;
import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import bg.nbu.cscb532.logistics.data.repository.ShippingRepository;
import bg.nbu.cscb532.logistics.data.repository.ShippingStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;
    private final ShippingStatusRepository shippingStatusRepository;
    private final UserService userService;
    private final CityService cityService;
    private final OfficeService officeService;
    private final AuthService authService;
    private final ServiceService serviceService;

    @Override
    public Shipping save(SaveShippingDto shippingDto) {
        Shipping shipping = createEntity(shippingDto);

        shippingRepository.save(shipping);
        shippingStatusRepository.saveAll(shipping.getShippingStatuses());

        return shipping;
    }

    @Override
    public Shipping createEntity(SaveShippingDto shippingDto) {
        User creator = authService.getLoggedInUser();
        Shipping shipping;

        if (shippingDto.getId() == null) {
            shipping = new Shipping();

            shipping.getShippingStatuses().add(
                    ShippingStatus.builder()
                            .type(ShippingStatusType.NEW)
                            .user(creator)
                            .shipping(shipping)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
            shipping.setCreatedAt(LocalDateTime.now());
        } else {
            shipping = shippingRepository.findById(shippingDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));
        }

        ShippingStatus lastStatus = getLastStatusType(shipping)
                .orElseThrow(() -> new IllegalArgumentException("Last status cannot be detected."));

        if (lastStatus.getType() != ShippingStatusType.NEW) {
            throw new IllegalArgumentException("Only new shippings can be saved");
        }

        User sender = userService.findById(shippingDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        shipping.setSender(sender);

        if (shippingDto.getSenderOfficeId() == null) {
            City senderCity = cityService.findById(shippingDto.getSenderCityId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender city not found"));
            shipping.setSenderAddress(new Address(shippingDto.getSenderAddressLine(), senderCity));
        } else {
            Office senderOffice = officeService.findById(shippingDto.getSenderOfficeId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender office not found"));
            shipping.setSenderOffice(senderOffice);
        }

        User receiver = userService.findById(shippingDto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        shipping.setReceiver(receiver);

        if (shippingDto.getReceiverOfficeId() == null) {
            City receiverCity = cityService.findById(shippingDto.getReceiverCityId())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver city not found"));
            shipping.setReceiverAddress(new Address(shippingDto.getReceiverAddressLine(), receiverCity));
        } else {
            Office receiverOffice = officeService.findById(shippingDto.getReceiverOfficeId())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver office not found"));
            shipping.setReceiverOffice(receiverOffice);
        }

        shipping.setWeightInGrams(shippingDto.getWeightGrams());
        shipping.setContentDescription(shippingDto.getDescription());

        shipping.setTotalPriceEur(serviceService.getAmountEur(shipping));

        return shipping;
    }

    @Override
    public List<Shipping> findAll() {
        return shippingRepository.findAll();
    }

    @Override
    public Optional<ShippingStatus> getLastStatusType(Shipping shipping) {
        return shipping.getShippingStatuses()
                .stream()
                .max(Comparator.comparing(ShippingStatus::getCreatedAt));
    }
}
