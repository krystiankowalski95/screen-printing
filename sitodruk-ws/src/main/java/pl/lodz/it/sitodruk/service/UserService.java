package pl.lodz.it.sitodruk.service;

import org.apache.catalina.User;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.UserNotFoundException;
import pl.lodz.it.sitodruk.model.UserEntity;

public interface UserService {
    void createUser(UserDTO userDTO) throws BaseException;
    void modifyUser(UserDTO userDTO) throws BaseException;
    UserEntity findUserByUsername(String username) throws BaseException;

}
