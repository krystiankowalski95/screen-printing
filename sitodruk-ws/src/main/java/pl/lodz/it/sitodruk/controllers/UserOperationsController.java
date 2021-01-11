package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.service.UserService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional(propagation = Propagation.NEVER)
public class UserOperationsController {

    @Autowired
    private UserService userService;

    private Properties exceptionProperties;

    @PostMapping("/confirmAccount")
    @PermitAll
    public ResponseEntity<?> confirmAccount(@RequestBody Map<String,String> tokenRequest){
            try {
                userService.confirmUser(tokenRequest.get("token"));
            } catch (BaseException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
            }
        return  ResponseEntity.ok("account.confirmed");
    }

    @PostMapping("/changePassword")
    @PermitAll
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO){
        try {
            userService.changePassword(userDTO);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
        return  ResponseEntity.ok("password.changed");
    }

    @PostMapping("/resetPassword")
    @PermitAll
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO,HttpServletRequest requestUrl){
        try {
            userService.resetPassword(userDTO,requestUrl);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
        return  ResponseEntity.ok("email.sent");
    }

    @PostMapping("/setNewPassword")
    @PermitAll
    public ResponseEntity<?> setNewPassword(@RequestBody UserDTO userDTO){
        try {
            userService.setNewPassword(userDTO);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
        return  ResponseEntity.ok("password.changed");
    }
}
