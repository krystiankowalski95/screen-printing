package pl.lodz.it.sitodruk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.OrderStatusEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity,Long> {
}
