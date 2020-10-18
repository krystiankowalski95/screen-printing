package pl.lodz.it.sitodruk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.it.sitodruk.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
}
