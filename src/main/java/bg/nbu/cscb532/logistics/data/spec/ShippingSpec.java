package bg.nbu.cscb532.logistics.data.spec;

import bg.nbu.cscb532.logistics.data.entity.*;
import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

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

    public static Specification<Shipping> createdAtMin(LocalDate minDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Shipping_.CREATED_AT), minDate);
    }

    public static Specification<Shipping> createdAtMax(LocalDate maxDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Shipping_.CREATED_AT), maxDate);
    }

    public static Specification<Shipping> lastStatusIs(ShippingStatusType shippingStatusType) {
        return (root, query, criteriaBuilder) -> {
            // Create subquery to find the latest ShippingStatus per Shipping
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ShippingStatus> subRoot = subquery.from(ShippingStatus.class);

            // Select max ID for each Shipping Status
            subquery.select(criteriaBuilder.max(subRoot.get(ShippingStatus_.ID)))
                .where(
                    criteriaBuilder.equal(subRoot.get(ShippingStatus_.SHIPPING), root)
                );

            // Main query: Only keep shippings where the latest status matches the given value
            Join<Object, Object> join = root.join(Shipping_.SHIPPING_STATUSES);

            return criteriaBuilder.and(
                criteriaBuilder.equal(
                    join.get(ShippingStatus_.ID),
                    subquery
                ),
                criteriaBuilder.equal(join.get(ShippingStatus_.TYPE), shippingStatusType.name())
            );
        };
    }
}
