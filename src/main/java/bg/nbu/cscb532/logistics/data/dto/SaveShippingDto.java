package bg.nbu.cscb532.logistics.data.dto;

import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SaveShippingDto {
    private Long id;

    @NotNull
    private Long senderId;

    @NotNull
    private String collectionType;

    @Size(min = 3)
    private String senderAddressLine;

    private Long senderCityId;

    private Long senderOfficeId;

    @NotNull
    private Long receiverId;

    @NotNull
    private String deliveryType;

    @Size(min = 3)
    private String receiverAddressLine;

    private Long receiverCityId;

    private Long receiverOfficeId;

    @NotNull
    private Integer weightGrams;

    @Size(min = 3, max = 255)
    private String description;

    public static SaveShippingDto fromEntity(Shipping shipping) {
        SaveShippingDto dto =SaveShippingDto.builder()
                .id(shipping.getId())
                .senderId(shipping.getSender().getId())
                .receiverId(shipping.getReceiver().getId())
                .weightGrams(shipping.getWeightInGrams())
                .description(shipping.getContentDescription())
                .build();

        if (shipping.getSenderOffice() != null) {
            dto.setCollectionType(ServiceType.COLLECTION_FROM_OFFICE.name());
            dto.setSenderOfficeId(shipping.getSenderOffice().getId());
        } else {
            dto.setCollectionType(ServiceType.COLLECTION_FROM_ADDRESS.name());
            dto.setSenderAddressLine(shipping.getSenderAddress().getAddressLine());
            dto.setSenderCityId(shipping.getSenderAddress().getCity().getId());
        }

        if (shipping.getReceiverOffice() != null) {
            dto.setDeliveryType(ServiceType.DELIVERY_TO_OFFICE.name());
            dto.setReceiverOfficeId(shipping.getReceiverOffice().getId());
        } else {
            dto.setDeliveryType(ServiceType.DELIVERY_TO_ADDRESS.name());
            dto.setReceiverAddressLine(shipping.getReceiverAddress().getAddressLine());
            dto.setReceiverCityId(shipping.getReceiverAddress().getCity().getId());
        }

        return dto;
    }
}
