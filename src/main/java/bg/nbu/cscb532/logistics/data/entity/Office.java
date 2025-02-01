package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Office extends BaseEntity {
    @NotNull
    private String name;

    @Embedded
    private Address address;

    @Override
    public String toString() {
        return "Office %s, %s".formatted(name, address);
    }
}
