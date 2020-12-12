package pl.lodz.it.sitodruk.repositories.mop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mop.CategoryEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mopTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface ProductCategoryRepository extends JpaRepository<CategoryEntity,Long> {
}
