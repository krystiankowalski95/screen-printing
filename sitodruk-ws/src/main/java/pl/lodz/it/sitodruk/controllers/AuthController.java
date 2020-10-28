package pl.lodz.it.sitodruk.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.it.sitodruk.config.JwtUtils;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.impl.UserDetailsImpl;
import pl.lodz.it.sitodruk.model.UserEntity;
import pl.lodz.it.sitodruk.model.UserAccessLevelEntity;
import pl.lodz.it.sitodruk.payload.JwtResponse;
import pl.lodz.it.sitodruk.payload.LoginRequest;
import pl.lodz.it.sitodruk.payload.MessageResponse;
import pl.lodz.it.sitodruk.payload.SignupRequest;
import pl.lodz.it.sitodruk.repositories.UserAccessLevelRepository;
import pl.lodz.it.sitodruk.repositories.UserRepository;
import pl.lodz.it.sitodruk.service.EmailSenderService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserAccessLevelRepository userAccessLevelRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if(userRepository.existsByUsernameAndPasswordAndConfirmed(userDTO.getUsername(), userDTO.getPassword(), false)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Account is not confirmed!"));
        }

        if(userRepository.existsByUsernameAndPasswordAndActive(userDTO.getUsername(), userDTO.getPassword(), false)){
            userRepository.existsByUsernameAndPasswordAndConfirmed("is active= "+userDTO.getUsername(),userDTO.getPassword(),true);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Account is not active!"));
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        UserEntity user = new UserEntity();
        user.setConfirmed(false);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstName());
        user.setLastname(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setConfirmed(false);
        user.setActive(true);
        UserAccessLevelEntity userAccessLevelEntity = new UserAccessLevelEntity();
        userAccessLevelEntity.setAccessLevelName("ROLE_CLIENT");
        userAccessLevelEntity.setActive(true);
        userAccessLevelEntity.setLoginDataByUserId(user);
        user.getUserAccessLevelsById().add(userAccessLevelEntity);
        userRepository.save(user);
        emailSenderService.sendSimpleMessage(user.getEmail(),"Witaj","Witaj w naszym systemie rozdawaniu hajsu");
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}