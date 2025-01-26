package bg.nbu.cscb532.logistics.data.entity;

import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShippingStatus extends BaseEntity {
    @ManyToOne
    private Shipping shipping;

    @Enumerated(value = EnumType.STRING)
    private ShippingStatusType type;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;
}
