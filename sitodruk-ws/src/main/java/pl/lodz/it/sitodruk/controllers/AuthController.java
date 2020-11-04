package pl.lodz.it.sitodruk.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.config.JwtUtils;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.impl.UserDetailsImpl;
import pl.lodz.it.sitodruk.payload.JwtResponse;
import pl.lodz.it.sitodruk.payload.MessageResponse;
import pl.lodz.it.sitodruk.service.UserService;
import pl.lodz.it.sitodruk.utils.ResourceBundles;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    private Properties exceptionProperties;

    @PostConstruct
    private void init() {
        try {
            exceptionProperties = ResourceBundles.loadProperties("exception.properties");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @PostMapping("/signin")
    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> authenticateUser(@RequestBody UserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        try {
            if (userService.isUserConfirmed(userDTO)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("account.notconfirmed"));
            } else if (userService.isUserActive(userDTO)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("account.notactive"));
            }
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }


    @PostMapping("/signup")
    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            userService.createUser(userDTO, request);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}