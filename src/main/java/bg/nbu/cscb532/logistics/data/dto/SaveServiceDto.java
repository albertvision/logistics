package bg.nbu.cscb532.logistics.data.dto;

import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveServiceDto {
    private Long id;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    @Min(0)
    private Integer minWeight;

    @NotNull
    @Min(0)
    private BigDecimal basePriceEur;

    @NotNull
    @Min(0)
    private BigDecimal priceEurPerWeight;
}
