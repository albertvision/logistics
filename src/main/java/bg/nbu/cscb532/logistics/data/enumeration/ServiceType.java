package bg.nbu.cscb532.logistics.data.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceType {
    TRANSPORTATION("Transportation"),
    COLLECTION_FROM_OFFICE("Collection from office"),
    COLLECTION_FROM_ADDRESS("Collection from address"),
    DELIVERY_TO_OFFICE("Delivery to office"),
    DELIVERY_TO_ADDRESS("Delivery to address"),
    ;

    private final String label;
}
