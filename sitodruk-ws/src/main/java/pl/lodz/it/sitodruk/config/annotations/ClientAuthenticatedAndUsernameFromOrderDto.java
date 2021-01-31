package pl.lodz.it.sitodruk.config.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(pl.lodz.it.sitodruk.SecurityConsts).CLIENT) && #orderDTO.username == principal.username")
public @interface ClientAuthenticatedAndUsernameFromOrderDto {
}