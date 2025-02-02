package bg.nbu.cscb532.logistics.data.repository;

import bg.nbu.cscb532.logistics.data.entity.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long> {
}
