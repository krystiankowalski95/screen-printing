package pl.lodz.it.sitodruk.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.config.JwtUtils;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.impl.UserDetailsImpl;
import pl.lodz.it.sitodruk.payload.JwtResponse;
import pl.lodz.it.sitodruk.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER,transactionManager = "mokTransactionManager")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    @PermitAll
    public ResponseEntity<?> authenticateUser(@RequestBody UserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        try {
            if (!userService.isUserConfirmed(userDTO)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"account.notconfirmed");

            } else if (!userService.isUserActive(userDTO)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"account.notactive");
            }
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }


    @PostMapping("/signup")
    @PermitAll
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            userService.createUser(userDTO, request);
            return ResponseEntity.ok("user.registered");

        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }
}