package pl.lodz.it.sitodruk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.it.sitodruk.model.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
}
