package bg.nbu.cscb532.logistics.data.spec;

import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.entity.Shipping_;
import bg.nbu.cscb532.logistics.data.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class ShippingSpec {
    public static Specification<Shipping> idIs(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Shipping_.ID), id);
    }

    public static Specification<Shipping> senderIs(User user) {
        return (root, query, cb) -> cb.equal(root.get(Shipping_.SENDER), user);
    }

    public static Specification<Shipping> receiverIs(User user) {
        return (root, query, cb) -> cb.equal(root.get(Shipping_.RECEIVER), user);
    }
}
