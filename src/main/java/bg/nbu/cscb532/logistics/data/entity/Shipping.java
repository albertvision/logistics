package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shipping extends BaseEntity {
    private LocalDateTime createdAt;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private Integer weightInGrams;

    private BigDecimal totalPriceEur;

    @OneToMany(mappedBy = "shipping")
    private Set<ShippingStatus> shippingStatuses = new LinkedHashSet<>();

}
