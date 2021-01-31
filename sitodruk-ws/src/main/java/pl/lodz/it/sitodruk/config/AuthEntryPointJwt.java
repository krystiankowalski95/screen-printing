package pl.lodz.it.sitodruk.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException, ResponseStatusException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        if(authException.getMessage().contains("Bad credentials")){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"bad.credentials");
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");

        }
    }

}