package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.payload.JwtResponse;
import pl.lodz.it.sitodruk.service.UserService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class ConfirmAccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirmAccount")
    @PermitAll
    public ResponseEntity<?> confirmAccount(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        String token = "";
        if (url.contains("token=")){
            token = url.substring(url.indexOf("token=") + 6);
            try {
                userService.confirmUser(token);
                return new ResponseEntity(HttpStatus.OK);
            } catch (BaseException e) {
                e.printStackTrace();
            }
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return null;
    }

}
