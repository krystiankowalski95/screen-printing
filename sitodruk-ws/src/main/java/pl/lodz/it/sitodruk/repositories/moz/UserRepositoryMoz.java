package pl.lodz.it.sitodruk.repositories.moz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.moz.UserEntity;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY, transactionManager = "mozTransactionManager",isolation = Isolation.READ_COMMITTED)
public interface UserRepositoryMoz extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByToken(String token);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByUsernameAndPasswordAndConfirmed(String username,String password,Boolean confirmed);
    Boolean existsByUsernameAndPasswordAndActive(String username,String password,Boolean active);
}
