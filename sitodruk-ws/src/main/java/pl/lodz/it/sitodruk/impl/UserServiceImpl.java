package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.dto.mappers.UserMapper;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.model.UserAccessLevelEntity;
import pl.lodz.it.sitodruk.model.UserEntity;
import pl.lodz.it.sitodruk.repositories.UserRepository;
import pl.lodz.it.sitodruk.service.EmailSenderService;
import pl.lodz.it.sitodruk.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder encoder;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createUser(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        } else if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException();
        } else {
            UserEntity user = UserMapper.INSTANCE.createNewUser(userDTO);
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            UserAccessLevelEntity userAccessLevelEntity = new UserAccessLevelEntity();
            userAccessLevelEntity.setAccessLevelName("ROLE_CLIENT");
            userAccessLevelEntity.setActive(true);
            userAccessLevelEntity.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(userAccessLevelEntity);
            userRepository.saveAndFlush(user);
            emailSenderService.sendRegistrationEmail(user.getEmail(), requestUrl, user.getToken());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void modifyUser(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            //TODO Mapowanie DTO na Encję
        } else throw new UserNotFoundException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public UserDTO findUserByUsername(String username) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return userMapper.toUserDTO(user.get());
        } else throw new UserNotFoundException();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void confirmUser(String token) throws BaseException {
        Optional<UserEntity> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            user.get().setConfirmed(true);
            userRepository.saveAndFlush(user.get());
        } else throw new UserNotFoundException();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean isUserConfirmed(UserDTO userDTO) {
        return userRepository.existsByUsernameAndPasswordAndConfirmed(userDTO.getUsername(), userDTO.getPassword(), false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean isUserActive(UserDTO userDTO) {
        return userRepository.existsByUsernameAndPasswordAndActive(userDTO.getUsername(), userDTO.getPassword(), false);
    }

}
