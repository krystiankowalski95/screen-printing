package pl.lodz.it.sitodruk.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
}