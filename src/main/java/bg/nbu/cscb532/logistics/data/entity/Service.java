package bg.nbu.cscb532.logistics.data.entity;

import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {Service_.SERVICE_TYPE, Service_.MIN_WEIGHT})
})
public class Service extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Column(nullable = false)
    private Integer minWeight;

    @Column(nullable = false)
    private BigDecimal basePriceEur;

    @Column(nullable = false)
    private BigDecimal priceEurPerWeight;
}
