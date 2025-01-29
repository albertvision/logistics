package bg.nbu.cscb532.logistics.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
    private String name;
    private String address;

    @ManyToOne
    private City city;
}
