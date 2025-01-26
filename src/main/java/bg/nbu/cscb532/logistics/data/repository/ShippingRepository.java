package bg.nbu.cscb532.logistics.data.repository;

import bg.nbu.cscb532.logistics.data.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

}
