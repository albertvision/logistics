package bg.nbu.cscb532.logistics.data.dto;

import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShippingListDto {

    private LocalDate createdAtMin;

    private LocalDate createdAtMax;

    private ShippingStatusType status;

    private Long receiverId;
}
