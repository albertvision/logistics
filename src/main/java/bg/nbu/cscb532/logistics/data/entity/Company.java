package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    private Long id;

    private String name;

    private BigDecimal deliveryToLocationFeeEur;

    private BigDecimal deliveryToOfficeFeeEur;
}
