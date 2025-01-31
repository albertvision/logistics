package bg.nbu.cscb532.logistics.data.spec;

import bg.nbu.cscb532.logistics.data.entity.Service;
import bg.nbu.cscb532.logistics.data.entity.Service_;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ServiceSpec {
    public static Specification<Service> minWeightLessOrEqualTo(Integer value) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Service_.MIN_WEIGHT), value);
    }

    public static Specification<Service> typeIs(ServiceType type) {
        return (root, query, cb) -> cb.equal(root.get(Service_.SERVICE_TYPE), type.name());
    }

    public static Sort sortByMinWeightDesc() {
        return Sort.by(Sort.Direction.DESC, Service_.MIN_WEIGHT);
    }
}
