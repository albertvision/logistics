package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {
    @NotNull
    private String addressLine;

    @ManyToOne
    private City city;

    @Override
    public String toString() {
        return "%s, %s".formatted(city, addressLine);
    }
}
