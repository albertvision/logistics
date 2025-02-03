package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.dto.ShippingListDto;
import bg.nbu.cscb532.logistics.data.entity.*;
import bg.nbu.cscb532.logistics.data.enumeration.Authority;
import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import bg.nbu.cscb532.logistics.data.repository.ShippingRepository;
import bg.nbu.cscb532.logistics.data.repository.ShippingStatusRepository;
import bg.nbu.cscb532.logistics.data.spec.ShippingSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    public Optional<Shipping> findById(Long id) {
        return shippingRepository.findBy(
                getBaseShippingSpec()
                        .and(ShippingSpec.idIs(id)),
                FluentQuery.FetchableFluentQuery::first
        );
    }

    @Override
    @Transactional
    public Shipping save(SaveShippingDto shippingDto) {
        Shipping shipping = createEntity(shippingDto);

        if (shipping.getId() == null) {
            shippingRepository.save(shipping);
            shippingStatusRepository.saveAll(shipping.getShippingStatuses());
        } else {
            shippingStatusRepository.saveAll(shipping.getShippingStatuses());
            shippingRepository.save(shipping);
        }

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
            shipping = findById(shippingDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));
        }

        ShippingStatus lastStatus = getLastStatusType(shipping)
                .orElseThrow(() -> new IllegalArgumentException("Last status cannot be detected."));

        User sender = userService.findById(shippingDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        if (!getAllowedSenders().contains(sender)) {
            throw new IllegalArgumentException("Sender not allowed");
        }

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

        ShippingStatusType shippingStatusType = shippingDto.getShippingStatus();
        if (!getAllowedStatusType(shipping).contains(shippingStatusType) ) {
            throw new IllegalArgumentException("Shipping status not allowed");
        }

        if (!shippingStatusType.equals(lastStatus.getType())) {
            shipping.getShippingStatuses().add(
                ShippingStatus.builder()
                    .type(shippingStatusType)
                    .user(creator)
                    .shipping(shipping)
                    .createdAt(LocalDateTime.now())
                    .build()
            );
        }

        shipping.setWeightInGrams(shippingDto.getWeightGrams());
        shipping.setContentDescription(shippingDto.getDescription());

        shipping.setTotalPriceEur(serviceService.getAmountEur(shipping));

        return shipping;
    }

    @Override
    public List<Shipping> findAll(ShippingListDto shippingListDto) {
        Specification<Shipping> specification = getBaseShippingSpec();

        // Filters
        if (shippingListDto.getStatus() != null) {
            specification = specification.and(ShippingSpec.lastStatusIs(shippingListDto.getStatus()));
        }

        if (shippingListDto.getReceiverId() != null) {
            Optional<User> receiver = userService.findById(shippingListDto.getReceiverId());

            if (receiver.isPresent()) {
                specification = specification.and(ShippingSpec.receiverIs(receiver.get()));
            }
        }

        return shippingRepository.findAll(specification);
    }

    private Specification<Shipping> getBaseShippingSpec() {
        User currentUser = authService.getLoggedInUser();
        Specification<Shipping> specification = Specification.where(null);

        if (currentUser.getAuthority().equals(Authority.USER)) {
            specification = specification.and(
                    ShippingSpec.senderIs(currentUser)
                            .or(ShippingSpec.receiverIs(currentUser)
                            )
            );
        }

        return specification;
    }

    @Override
    public Optional<ShippingStatus> getLastStatusType(Shipping shipping) {
        return shipping.getShippingStatuses()
                .stream()
                .max(Comparator.comparing(ShippingStatus::getCreatedAt));
    }

    @Override
    public List<ShippingStatusType> getAllowedStatusType(Shipping shipping) {
        Optional<ShippingStatus> lastStatus = getLastStatusType(shipping);

        if (lastStatus.isEmpty()) {
            return List.of(ShippingStatusType.NEW);
        }

        if(authService.getLoggedInUser().getAuthority().equals(Authority.USER)) {
            return List.of(lastStatus.get().getType());
        }

        List<ShippingStatusType> statusTypesToRemove = new ArrayList<>();
        List<ShippingStatusType> statusTypes = new java.util.LinkedList<>(Arrays.stream(ShippingStatusType.values()).toList());

        for (ShippingStatusType statusType : statusTypes) {
            if (statusType.equals(lastStatus.get().getType())) {
                break;
            }

            statusTypesToRemove.add(statusType);
        }

        statusTypes.removeAll(statusTypesToRemove);

        return statusTypes;
    }

    @Override
    public List<ShippingStatusType> getAllowedStatusType(SaveShippingDto shippingDto) {
        Shipping shipping = shippingDto.getId() == null
            ? Shipping.builder().build()
            : findById(shippingDto.getId()).orElseThrow();

        return getAllowedStatusType(shipping);
    }

    @Override
    public List<User> getAllowedSenders() {
        User currentUser = authService.getLoggedInUser();

        if (currentUser.getAuthority().equals(Authority.USER)) {
            return List.of(currentUser);
        }

        return userService.findAll();
    }
}
