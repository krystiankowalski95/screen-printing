package pl.lodz.it.sitodruk.repositories.mop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    ProductEntity findByNameAndCategoryName(String name,String categoryName);
    Boolean existsByNameAndCategoryName(String name,String categoryName);
    Optional<ProductEntity> findByName(String productName);
}