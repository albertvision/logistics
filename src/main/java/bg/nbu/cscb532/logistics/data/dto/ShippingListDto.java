package bg.nbu.cscb532.logistics.data.dto;

import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShippingListDto {
    private ShippingStatusType status;

    private Long receiverId;
}
