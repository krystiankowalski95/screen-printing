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
import pl.lodz.it.sitodruk.SecurityConsts;
import pl.lodz.it.sitodruk.config.annotations.AnyUserAuthenticatedAndDtoUsername;
import pl.lodz.it.sitodruk.config.annotations.AnyUserAuthenticatedAndMapUsername;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.service.UserService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/app/users")
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
        }catch (TokenExpiredException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/activateAccount")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> activateAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.activateUserAccount(userDTO);
            return ResponseEntity.ok("user.activated");
        }catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/deactivateAccount")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> deactivateAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.deactivateUserAccount(userDTO);
            return ResponseEntity.ok("user.deactivated");
        }catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        }catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/resendActivationLink")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> resendActivationLink(@RequestBody UserDTO userDTO,HttpServletRequest httpServletRequest) {
        try {
            userService.sendActivationLink(userDTO,httpServletRequest);
            return ResponseEntity.ok("email.send");
        }catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


    @PostMapping("/activateAccessLevel/{role}")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> activateAccessLevel(@RequestBody UserDTO userDTO,@PathVariable String role) {
        try {
            userService.activateUserAccessLevel(userDTO, role);
            return ResponseEntity.ok("access.level.activated");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/deactivateAccessLevel/{role}")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> deactivateAccessLevel(@RequestBody UserDTO userDTO,@PathVariable String role) {
        try {
            userService.deactivateUserAccessLevel(userDTO, role);
            return ResponseEntity.ok("access.level.deactivated");
        } catch (UserAccessLevelDeactivationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> createUserByAdmin(@RequestBody UserDTO userDTO) {
        try {
            userService.createUserByAdmin(userDTO);
            return ResponseEntity.ok("account.created");
        } catch (UsernameAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (EmailAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


    @PostMapping("/modifyOwnAccount")
    @AnyUserAuthenticatedAndDtoUsername
    public ResponseEntity<?> editOwnAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.modifyUser(userDTO);
            return ResponseEntity.ok("account.modified");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/modifyAccount")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> editAccount(@RequestBody UserDTO userDTO) {
        try {
            userService.modifyUser(userDTO);
            return ResponseEntity.ok("account.modified");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/changeOwnPassword")
    @AnyUserAuthenticatedAndDtoUsername
    public ResponseEntity<?> changeOwnPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO) {
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/resetPassword")
    @PermitAll
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO, HttpServletRequest requestUrl) {
        try {
            userService.resetPassword(userDTO, requestUrl);
            return ResponseEntity.ok("email.sent");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/setNewPassword")
    @PermitAll
    public ResponseEntity<?> setNewPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.setNewPassword(userDTO);
            return ResponseEntity.ok("password.changed");
        } catch (TokenExpiredException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


    @PostMapping("/findAccountByUsername")
    @AnyUserAuthenticatedAndMapUsername
    public ResponseEntity<UserDTO> getAccountByUsername(@RequestBody Map<String, String> username) {
        try {
            return new ResponseEntity(userService.findUserByUsername(username.get("username")), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/findAccount")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<UserDTO> getAccountAdminByUsername(@RequestBody Map<String, String> username) {
        try {
            return new ResponseEntity(userService.findUserByUsername(username.get("username")), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @GetMapping("/findAllAccounts")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.ADMIN + "')")
    public ResponseEntity<List<UserDTO>> findAllAcounts() {
        try {
            return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }
}
