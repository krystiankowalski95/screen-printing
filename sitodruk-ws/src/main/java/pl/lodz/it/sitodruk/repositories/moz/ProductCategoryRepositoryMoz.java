package pl.lodz.it.sitodruk.repositories.moz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.moz.CategoryEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mozTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface ProductCategoryRepositoryMoz extends JpaRepository<CategoryEntity,Long> {
}
