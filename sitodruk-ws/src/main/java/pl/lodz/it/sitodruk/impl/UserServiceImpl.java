package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.dto.mappers.UserMapper;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.model.mok.UserAccessLevelEntity;
import pl.lodz.it.sitodruk.model.mok.UserEntity;
import pl.lodz.it.sitodruk.repositories.mok.UserRepository;
import pl.lodz.it.sitodruk.service.EmailSenderService;
import pl.lodz.it.sitodruk.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW , transactionManager = "mokTransactionManager")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    PasswordEncoder encoder;

    @Override
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
            UserAccessLevelEntity client = new UserAccessLevelEntity();
            client.setAccessLevelName("ROLE_CLIENT");
            client.setActive(true);
            client.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(client);
            UserAccessLevelEntity manager = new UserAccessLevelEntity();
            manager.setAccessLevelName("ROLE_MANAGER");
            manager.setActive(false);
            manager.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(manager);
            UserAccessLevelEntity admin = new UserAccessLevelEntity();
            admin.setAccessLevelName("ROLE_ADMIN");
            admin.setActive(false);
            admin.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(admin);
            userRepository.saveAndFlush(user);
            emailSenderService.sendRegistrationEmail(user.getEmail(), requestUrl, user.getToken());
        }
    }

    @Override
    public void modifyUser(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            //TODO Mapowanie DTO na Encję
        } else throw new UserNotFoundException();
    }

    @Override
    public void setNewPassword(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByToken(userDTO.getToken());
        if(user.isPresent()){
            user.get().setPassword(encoder.encode(userDTO.getPassword()));
            userRepository.saveAndFlush(user.get());
        }else throw new UserNotFoundException();
    }

    @Override
    public void changePassword(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isPresent()){
            user.get().setPassword(encoder.encode(userDTO.getPassword()));
            userRepository.saveAndFlush(user.get());
        }else throw new UserNotFoundException();
    }

    @Override
    public UserDTO findUserByUsername(String username) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return UserMapper.INSTANCE.toUserDTO(user.get());
        } else throw new UserNotFoundException();
    }

    @Override
    public void resetPassword(UserDTO userDTO,HttpServletRequest requestUrl) throws BaseException {
        Optional<UserEntity> user = userRepository.findByEmail(userDTO.getEmail());
        if(user.isPresent()){
            user.get().setToken(UUID.randomUUID().toString().replace("-", ""));
            userRepository.saveAndFlush(user.get());
            emailSenderService.sendPasswordChangeEmail(user.get().getEmail(),requestUrl,user.get().getToken());
        }else throw new UserNotFoundException();
    }

    @Override
    public void confirmUser(String token) throws BaseException {
        Optional<UserEntity> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            if(user.get().getConfirmed()){
                throw new UserAlreadyConfirmedException();
            }else{
                user.get().setConfirmed(true);
                user.get().setActive(true);
                userRepository.saveAndFlush(user.get());
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public Boolean isUserConfirmed(UserDTO userDTO) {
        return userRepository.existsByUsernameAndPasswordAndConfirmed(userDTO.getUsername(), userDTO.getPassword(), false);
    }

    @Override
    public Boolean isUserActive(UserDTO userDTO) {
        return userRepository.existsByUsernameAndPasswordAndActive(userDTO.getUsername(), userDTO.getPassword(), false);
    }

}
