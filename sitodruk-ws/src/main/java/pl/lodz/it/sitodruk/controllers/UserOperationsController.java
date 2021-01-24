package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.service.UserService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER, transactionManager = "mokTransactionManager")
public class UserOperationsController {

    @Autowired
    private UserService userService;


    @PostMapping("/confirmAccount")
    @PermitAll
    public ResponseEntity<?> confirmAccount(@RequestBody Map<String, String> tokenRequest) {
        try {
            userService.confirmUser(tokenRequest.get("token"));
            return ResponseEntity.ok("account.confirmed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "order.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/activateAccount")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.activateUserAccount(userDTO);
            return ResponseEntity.ok("user.activated");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/deactivateAccount")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.deactivateUserAccount(userDTO);
            return ResponseEntity.ok("user.deactivated");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/resendActivationLink")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> resendActivationLink(@RequestBody UserDTO userDTO,HttpServletRequest httpServletRequest) {
        try {
            userService.sendActivationLink(userDTO,httpServletRequest);
            return ResponseEntity.ok("email.send");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }


    @PostMapping("/activateAccessLevel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateAccessLevel(@RequestBody UserDTO userDTO, String role) {
        try {
            userService.activateUserAccessLevel(userDTO, role);
            return ResponseEntity.ok("access.level.activated");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/deactivateAccessLevel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateAccessLevel(@RequestBody UserDTO userDTO, String role) {
        try {
            userService.activateUserAccessLevel(userDTO, role);
            return ResponseEntity.ok("access.level.deactivated");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUserByAdmin(@RequestBody UserDTO userDTO) {
        try {
            userService.createUserByAdmin(userDTO);
            return ResponseEntity.ok("account.created");
        } catch (UsernameAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username.already.exists", ex);
        } catch (EmailAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email.already.exists", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }


    @PostMapping("/modifyOwnAccount")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<?> editOwnAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.modifyUser(userDTO);
            return ResponseEntity.ok("account.modified");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/modifyAccount")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> editAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.modifyUser(userDTO);
            return ResponseEntity.ok("account.modified");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/changeOwnPassword")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<?> changeOwnPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO) {
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/resetPassword")
    @PermitAll
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO, HttpServletRequest requestUrl) {
        try {
            userService.resetPassword(userDTO, requestUrl);
            return ResponseEntity.ok("email.sent");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/setNewPassword")
    @PermitAll
    public ResponseEntity<?> setNewPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.setNewPassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }


    @PostMapping("/findAccountByUsername")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<UserDTO> getAccountByUsername(@RequestBody Map<String, String> username) {
        try {
            return new ResponseEntity(userService.findUserByUsername(username.get("username")), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/findAccount")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getAccountAdminByUsername(@RequestBody Map<String, String> username) {
        try {
            return new ResponseEntity(userService.findUserByUsername(username.get("username")), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @GetMapping("/findAllAccounts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAllAcounts() {
        try {
            return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }
}
