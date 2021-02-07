package pl.lodz.it.sitodruk.repositories.mok;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mok.UserEntity;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mokTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByToken(String token);
    Optional<UserEntity> findByPasswordToken(String token);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByUsernameAndPasswordAndConfirmed(String username,String password,Boolean confirmed);
    Boolean existsByUsernameAndPasswordAndActive(String username,String password,Boolean active);
}
