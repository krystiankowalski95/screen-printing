package pl.lodz.it.sitodruk.impl;

import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, transactionManager = "mokTransactionManager")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void activateUserAccount(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setActive(true);
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void deactivateUserAccount(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setActive(false);
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void activateUserAccessLevel(UserDTO userDTO, String role) throws BaseException {
        String appRoleName = "";
        if (role.equalsIgnoreCase("admin")) {
            appRoleName = "ROLE_ADMIN";
        } else if (role.equalsIgnoreCase("manager")) {
            appRoleName = "ROLE_MANAGER";
        } else if (role.equalsIgnoreCase("client")) {
            appRoleName = "ROLE_CLIENT";
        }
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                for (UserAccessLevelEntity userAccessLevelEntity : user.get().getUserAccessLevelsById()) {
                    if (userAccessLevelEntity.getAccessLevelName().equalsIgnoreCase(appRoleName)) {
                        userAccessLevelEntity.setActive(true);
                    }
                }
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void deactivateUserAccessLevel(UserDTO userDTO, String role) throws BaseException {
        String appRoleName = "";
        if (role.equalsIgnoreCase("admin")) {
            appRoleName = "ROLE_ADMIN";
        } else if (role.equalsIgnoreCase("manager")) {
            appRoleName = "ROLE_MANAGER";
        } else if (role.equalsIgnoreCase("client")) {
            appRoleName = "ROLE_CLIENT";
        }
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                List<UserAccessLevelEntity> list = user.get().getUserAccessLevelsById().stream().filter(UserAccessLevelEntity::getActive).collect(Collectors.toList());
                if (list.size() > 1) {
                    for (UserAccessLevelEntity userAccessLevelEntity : user.get().getUserAccessLevelsById()) {
                        if (userAccessLevelEntity.getAccessLevelName().equalsIgnoreCase(appRoleName)) {
                            userAccessLevelEntity.setActive(false);
                        }
                    }
                    userRepository.saveAndFlush(user.get());
                } else {
                    throw new UserAccessLevelDeactivationException();
                }
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void createUserByAdmin(UserDTO userDTO) throws BaseException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        } else {
            UserEntity user = UserMapper.INSTANCE.createNewUser(userDTO);
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setConfirmed(true);

            UserAccessLevelEntity client = new UserAccessLevelEntity();
            client.setAccessLevelName("ROLE_CLIENT");
            client.setActive(userDTO.getRoles().contains("client"));
            client.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(client);

            UserAccessLevelEntity manager = new UserAccessLevelEntity();
            manager.setAccessLevelName("ROLE_MANAGER");
            manager.setActive(userDTO.getRoles().contains("manager"));
            manager.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(manager);

            UserAccessLevelEntity admin = new UserAccessLevelEntity();
            admin.setAccessLevelName("ROLE_ADMIN");
            admin.setActive(userDTO.getRoles().contains("admin"));
            admin.setLoginDataByUserId(user);
            user.getUserAccessLevelsById().add(admin);
            userRepository.saveAndFlush(user);
        }

    }

    @Override
    public void sendActivationLink(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setToken(UUID.randomUUID().toString());
                userRepository.saveAndFlush(user.get());
                emailSenderService.sendRegistrationEmail(user.get().getEmail(), requestUrl, user.get().getToken());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void createUser(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        } else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        } else {
            UserEntity user = UserMapper.INSTANCE.createNewUser(userDTO);
            user.setPassword(encoder.encode(userDTO.getPassword()));
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
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setFirstname(userDTO.getFirstname());
                user.get().setLastname(userDTO.getLastname());
                user.get().setPhoneNumber(userDTO.getPhoneNumber());
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }


    @Override
    public void setNewPassword(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByToken(userDTO.getToken());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setPassword(encoder.encode(userDTO.getPassword()));
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void changePassword(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setPassword(encoder.encode(userDTO.getPassword()));
                userRepository.saveAndFlush(user.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public UserDTO findUserByUsername(String username) throws BaseException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user.get());
            userDTO.setDtoVersion(getVersionHash(user.get()));
            userDTO.setPassword("");
            for (UserAccessLevelEntity userAccessLevelEntity : user.get().getUserAccessLevelsById()) {
                if (userAccessLevelEntity.getActive()) {
                    userDTO.getRoles().add(userAccessLevelEntity.getAccessLevelName());
                }
            }
            return userDTO;
        } else throw new UserNotFoundException();
    }

    public List<UserDTO> getAllUsers() throws BaseException {
        List<UserDTO> userDtos = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(userEntity);
            userDTO.setDtoVersion(getVersionHash(userEntity));
            userDTO.setPassword("");
            for (UserAccessLevelEntity userAccessLevelEntity : userEntity.getUserAccessLevelsById()) {
                if (userAccessLevelEntity.getActive()) {
                    userDTO.getRoles().add(userAccessLevelEntity.getAccessLevelName());
                }
            }
            userDtos.add(userDTO);
        }
        return userDtos;
    }

    @Override
    public void resetPassword(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException {
        Optional<UserEntity> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isPresent()) {
            if (userDTO.getDtoVersion().equals(getVersionHash(user.get()))) {
                user.get().setToken(UUID.randomUUID().toString().replace("-", ""));
                userRepository.saveAndFlush(user.get());
                emailSenderService.sendPasswordChangeEmail(user.get().getEmail(), requestUrl, user.get().getToken());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public void confirmUser(String token) throws BaseException {
        Optional<UserEntity> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            if (user.get().getConfirmed()) {
                throw new UserAlreadyConfirmedException();
            } else {
                user.get().setConfirmed(true);
                user.get().setActive(true);
                userRepository.saveAndFlush(user.get());
            }
        } else throw new UserNotFoundException();
    }

    @Override
    public Boolean isUserConfirmed(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(userDTO.getUsername());
        if (userEntity.isPresent()) {
            return userEntity.get().getConfirmed();
        } else throw new UserNotFoundException();
    }

    @Override
    public Boolean isUserActive(UserDTO userDTO) throws BaseException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(userDTO.getUsername());
        if (userEntity.isPresent()) {
            return userEntity.get().getActive();
        } else throw new UserNotFoundException();
    }

    public String getVersionHash(UserEntity userEntity) {
        return DigestUtils.sha256Hex(userEntity.getEmail() + userEntity.getVersion());
    }

}
