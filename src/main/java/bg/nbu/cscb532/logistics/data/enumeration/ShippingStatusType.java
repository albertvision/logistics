package bg.nbu.cscb532.logistics.data.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShippingStatusType {
    NEW("New"),
    COLLECTED("Collected"),
    IN_TRANSIT("In Transit"),
    AT_OFFICE("At Office"),
    DELIVERED("Delivered");

    private final String label;
}
