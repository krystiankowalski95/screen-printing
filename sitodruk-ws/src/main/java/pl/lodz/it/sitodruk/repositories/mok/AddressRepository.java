package pl.lodz.it.sitodruk.repositories.mok;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mok.AddressEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mokTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
}
