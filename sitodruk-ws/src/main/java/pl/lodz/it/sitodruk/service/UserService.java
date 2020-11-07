package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void createUser(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException;
    void modifyUser(UserDTO userDTO) throws BaseException;
    UserDTO findUserByUsername(String username) throws BaseException;
    void confirmUser(String token) throws BaseException;
    Boolean isUserConfirmed(UserDTO userDTO) throws BaseException;
    Boolean isUserActive(UserDTO userDTO) throws BaseException;
}
