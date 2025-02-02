package bg.nbu.cscb532.logistics.data.dto;

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
}
