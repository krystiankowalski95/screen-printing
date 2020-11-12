package pl.lodz.it.sitodruk.repositories.moz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.moz.OrderEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mozTransactionManager")
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
}
