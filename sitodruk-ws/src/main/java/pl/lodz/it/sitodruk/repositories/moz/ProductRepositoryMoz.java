package pl.lodz.it.sitodruk.repositories.moz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.moz.ProductEntity;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mozTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface ProductRepositoryMoz extends JpaRepository<ProductEntity,Long> {
    ProductEntity findByNameAndCategoryName(String name, String categoryName);
    Boolean existsByNameAndCategoryName(String name,String categoryName);
    Optional<ProductEntity> findByName(String productName);
    Optional<ProductEntity> findByNameAndStockGreaterThanEqual(String name, Long quantity);
}
