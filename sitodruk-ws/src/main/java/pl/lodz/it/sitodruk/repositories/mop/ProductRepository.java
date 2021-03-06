package pl.lodz.it.sitodruk.repositories.mop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mopTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    Optional<ProductEntity> findByNameAndCategoryName(String name,String categoryName);
    Boolean existsByNameAndCategoryName(String name,String categoryName);
    Optional<ProductEntity> findByName(String productName);
    List<ProductEntity> findAllByIsActiveIsTrue();
}
