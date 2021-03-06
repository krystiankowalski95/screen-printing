package pl.lodz.it.sitodruk.config.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(pl.lodz.it.sitodruk.SecurityConsts).CLIENT) " +
        "|| hasRole(T(pl.lodz.it.sitodruk.SecurityConsts).EMPLOYEE)" +
        "|| (hasRole(T(pl.lodz.it.sitodruk.SecurityConsts).ADMIN) && #username.get(\"username\") == principal.username)")
public @interface AnyUserAuthenticatedAndMapUsername {
}