package bg.nbu.cscb532.logistics.data.entity;

import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ShippingStatus extends BaseEntity {
    @ManyToOne
    private Shipping shipping;

    @Enumerated(value = EnumType.STRING)
    private ShippingStatusType type;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;
}
