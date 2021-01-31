package pl.lodz.it.sitodruk.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void createUser(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException, SQLException;

    void modifyUser(UserDTO userDTO) throws BaseException, SQLException;

    void setNewPassword(UserDTO userDTO) throws BaseException, SQLException;

    void changePassword(UserDTO userDTO) throws BaseException, SQLException;

    void resetPassword(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException, SQLException;

    UserDTO findUserByUsername(String username) throws BaseException, SQLException;

    List<UserDTO> getAllUsers() throws BaseException, SQLException;

    void confirmUser(String token) throws BaseException, SQLException;

    Boolean isUserConfirmed(UserDTO userDTO) throws BaseException, SQLException;

    Boolean isUserActive(UserDTO userDTO) throws BaseException, SQLException;

    void activateUserAccount(UserDTO userDTO) throws BaseException, SQLException;

    void deactivateUserAccount(UserDTO userDTO) throws BaseException, SQLException;

    void activateUserAccessLevel(UserDTO userDTO,String role) throws BaseException, SQLException;

    void deactivateUserAccessLevel(UserDTO userDTO,String role) throws BaseException, SQLException;

    void createUserByAdmin(UserDTO userDTO) throws BaseException, SQLException;

    void sendActivationLink(UserDTO userDTO, HttpServletRequest requestUrl) throws BaseException, SQLException;

}
