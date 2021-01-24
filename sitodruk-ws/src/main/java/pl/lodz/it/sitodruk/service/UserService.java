package pl.lodz.it.sitodruk.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    void createUser(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException;

    void modifyUser(UserDTO userDTO) throws BaseException;

    void setNewPassword(UserDTO userDTO) throws BaseException;

    void changePassword(UserDTO userDTO) throws BaseException;

    void resetPassword(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException;

    UserDTO findUserByUsername(String username) throws BaseException;

    List<UserDTO> getAllUsers() throws BaseException;

    void confirmUser(String token) throws BaseException;

    Boolean isUserConfirmed(UserDTO userDTO) throws BaseException;

    Boolean isUserActive(UserDTO userDTO) throws BaseException;

    void activateUserAccount(UserDTO userDTO) throws BaseException;

    void deactivateUserAccount(UserDTO userDTO) throws BaseException;

    void activateUserAccessLevel(UserDTO userDTO,String role) throws BaseException;

    void deactivateUserAccessLevel(UserDTO userDTO,String role) throws BaseException;

    void createUserByAdmin(UserDTO userDTO) throws BaseException;

    void sendActivationLink(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException;

}
