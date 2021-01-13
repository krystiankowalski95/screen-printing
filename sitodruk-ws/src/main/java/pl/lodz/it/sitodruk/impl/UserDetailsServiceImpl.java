package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.mok.UserEntity;
import pl.lodz.it.sitodruk.repositories.mok.UserRepository;

import java.util.Optional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW , transactionManager = "mokTransactionManager")
public class UserDetailsServiceImpl implements UserDetailsService, pl.lodz.it.sitodruk.service.UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return UserDetailsImpl.build(user.get());
        }else throw new UsernameNotFoundException("User Not Found ");
    }

}