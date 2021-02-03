package pl.lodz.it.sitodruk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.model.mok.UserEntity;
import pl.lodz.it.sitodruk.repositories.auth.AuthUserRepository;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Retryable(maxAttempts = 5, include = {SQLException.class})
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW , transactionManager = "authTransactionManager", rollbackFor = BaseException.class)
public class UserDetailsServiceImpl implements UserDetailsService, pl.lodz.it.sitodruk.service.UserDetailsService {
    @Autowired
    AuthUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return UserDetailsImpl.build(user.get());
        }else throw new UsernameNotFoundException("user.not.found");
    }

}