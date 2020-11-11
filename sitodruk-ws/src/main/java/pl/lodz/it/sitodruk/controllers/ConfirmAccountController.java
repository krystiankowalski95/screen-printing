package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.payload.MessageResponse;
import pl.lodz.it.sitodruk.service.UserService;
import pl.lodz.it.sitodruk.utils.ResourceBundles;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class ConfirmAccountController {

    @Autowired
    private UserService userService;

    private Properties exceptionProperties;

    @PostConstruct
    private void init() {
        try {
            exceptionProperties = ResourceBundles.loadProperties("exception.properties");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @GetMapping("/confirmAccount")
    @PermitAll
    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> confirmAccount(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        String token = "";
        if (url.contains("token=")){
            token = url.substring(url.indexOf("token=") + 6);
            try {
                userService.confirmUser(token);
            } catch (BaseException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
            }
        }
        return  ResponseEntity.ok(new MessageResponse("Account has been confirmed"));
    }

}
