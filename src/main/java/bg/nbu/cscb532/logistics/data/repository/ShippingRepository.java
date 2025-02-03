package bg.nbu.cscb532.logistics.data.repository;

import bg.nbu.cscb532.logistics.data.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShippingRepository extends JpaRepository<Shipping, Long>, JpaSpecificationExecutor<Shipping> {

}
