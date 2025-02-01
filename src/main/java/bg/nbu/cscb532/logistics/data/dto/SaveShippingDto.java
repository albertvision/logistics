package bg.nbu.cscb532.logistics.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @Min(2)
    private String senderAddressLine;

    private Long senderCityId;

    private Long senderOfficeId;

    @NotNull
    private Long receiverId;

    @NotNull
    private String deliveryType;

    @Min(2)
    private String receiverAddressLine;

    private Long receiverCityId;

    private Long receiverOfficeId;

    @NotNull
    private Integer weightGrams;

    @Length(min = 3, max = 255)
    private String description;
}
