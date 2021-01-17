package pl.lodz.it.sitodruk.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private boolean active;
    private boolean confirmed;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String confirmPassword;
    private String token;
    private Set<String> roles;
    private String dtoVersion;
}