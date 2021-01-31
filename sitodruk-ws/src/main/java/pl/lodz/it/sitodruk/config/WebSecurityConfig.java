package pl.lodz.it.sitodruk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.lodz.it.sitodruk.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
         securedEnabled = true,
         jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] publicRoutes = new String[]{"/api/auth/signin", "/api/auth/signup", "/api/auth/refreshToken", "/confirmAccount", "/resetPassword","/setNewPassword","/products/findAllActive","/products","/products/findByName/**"};
        String[] clientRoutes = new String[]{"/findAccountByUsername", "/changeOwnPassword","/modifyOwnAccount","/orders/placeOrder","/orders/cancel","/orders/repeatPayment","/orders/findUsersOrders","/orders/findByPayUOrderId"};
        String[] managerRoutes = new String[]{"/products/categories","/products/edit","/products/findAll","/products/addNew","/products/activateProduct","/products/deactivateProduct","/products/","/products/","/products/","/orders/cancel","/orders/complete","/orders/findUsersOrders","/orders/findByPayUOrderId","/orders/findAllOrders"};
        String[] adminRoutes = new String[]{"/findAllAccounts","/findAccount","/changePassword","/modifyAccount","/createUser","/deactivateAccessLevel/**","/activateAccessLevel/**","/resendActivationLink","/deactivateAccount","/activateAccount"};



        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/test/**").permitAll()
//                .antMatchers("/*").permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
