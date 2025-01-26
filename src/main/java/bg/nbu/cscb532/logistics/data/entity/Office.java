package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.Entity;
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
    public String name;
    public String address;
    public String city;
    public String postalCode;
}
