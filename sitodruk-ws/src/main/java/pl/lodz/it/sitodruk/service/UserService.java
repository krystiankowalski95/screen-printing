package pl.lodz.it.sitodruk.service;

import org.apache.catalina.User;
import pl.lodz.it.sitodruk.model.UserEntity;

public interface UserService {
    void createUser(UserEntity userEntity);
    void modifyUser(UserEntity userEntity);
    UserEntity findUserByUsername(String username);

}
