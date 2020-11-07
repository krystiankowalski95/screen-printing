package pl.lodz.it.sitodruk.repositories.mok;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mok.UserAccessLevelEntity;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevelEntity,Long> {
    Optional<UserAccessLevelEntity> findAllByLoginDataByUserId(Long userId);
}
