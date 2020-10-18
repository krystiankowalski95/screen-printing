package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.UserNotFoundException;
import pl.lodz.it.sitodruk.model.UserEntity;
import pl.lodz.it.sitodruk.repositories.UserRepository;
import pl.lodz.it.sitodruk.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void createUser(UserDTO userDTO) {
        //TODO Zmienić UserEntity na DTO i dodać mapowanie
        //userRepository.saveAndFlush(userEntity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createUser(UserEntity userEntity) {
        //TODO Zmienić UserEntity na DTO i dodać mapowanie
        userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    @Override
    public void modifyUser(UserDTO userDTO) throws BaseException{
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isPresent()){
            //TODO Mapowanie DTO na Encję
        }else throw new UserNotFoundException();
    }

    @Transactional
    @Override
    public UserEntity findUserByUsername(String username) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user.get();
        }else throw new UserNotFoundException();
    }
}
