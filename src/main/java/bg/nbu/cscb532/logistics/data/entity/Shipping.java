package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shipping extends BaseEntity {
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @Column(nullable = false)
    private User sender;

    @Embedded
    @AssociationOverride(name = Address_.CITY, joinColumns = @JoinColumn(name = "sender_city"))
    @AttributeOverride(name = Address_.ADDRESS_LINE, column = @Column(name = "sender_address_line"))
    private Address senderAddress;

    @ManyToOne
    private Office senderOffice;

    @ManyToOne
    @Column(nullable = false)
    private User receiver;

    @Embedded
    @AssociationOverride(name = Address_.CITY, joinColumns = @JoinColumn(name = "receiver_city"))
    @AttributeOverride(name = Address_.ADDRESS_LINE, column = @Column(name = "receiver_address_line"))
    private Address receiverAddress;

    @ManyToOne
    private Office receiverOffice;

    @Column(nullable = false)
    private Integer weightInGrams;

    @Column(nullable = false)
    private BigDecimal totalPriceEur;

    @Column(nullable = false)
    private String contentDescription;

    @OneToMany(mappedBy = "shipping")
    @OrderBy("createdAt")
    private Set<ShippingStatus> shippingStatuses = new LinkedHashSet<>();

}
