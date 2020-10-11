package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.model.UserEntity;
import pl.lodz.it.sitodruk.repositories.UserRepository;
import pl.lodz.it.sitodruk.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void createUser(UserEntity userEntity) {
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void modifyUser(UserEntity userEntity) {

    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return null;
    }
}
