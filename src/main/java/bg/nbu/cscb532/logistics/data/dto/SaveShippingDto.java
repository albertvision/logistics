package bg.nbu.cscb532.logistics.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Min(2)
    private String senderAddressLine;

    private Long senderCityId;

    private Long senderOfficeId;

    @NotNull
    private Long receiverId;

    @Min(2)
    private String receiverAddressLine;

    private Long receiverCityId;

    private Long receiverOfficeId;

    @NotNull
    private Integer weightGrams;

    @Min(3)
    private String description;
}
