package mate.academy.spring.config;

import mate.academy.spring.model.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String roleNameUser = Role.RoleName.USER.name();
        String roleNameAdmin = Role.RoleName.ADMIN.name();
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/cinema-halls")
                .hasAnyRole(roleNameUser, roleNameAdmin)
                .antMatchers(HttpMethod.POST, "/cinema-halls").hasRole(roleNameAdmin)
                .antMatchers(HttpMethod.GET, "/movies").hasAnyRole(roleNameUser, roleNameAdmin)
                .antMatchers(HttpMethod.POST, "/movies").hasRole(roleNameAdmin)
                .antMatchers(HttpMethod.GET, "/movie-sessions/available")
                .hasAnyRole(roleNameUser, roleNameAdmin)
                .antMatchers(HttpMethod.POST, "/movie-sessions").hasRole(roleNameAdmin)
                .antMatchers(HttpMethod.PUT, "/movie-sessions/{id}").hasRole(roleNameAdmin)
                .antMatchers(HttpMethod.DELETE, "/movie-sessions/{id}").hasRole(roleNameAdmin)
                .antMatchers(HttpMethod.GET, "/orders").hasRole(roleNameUser)
                .antMatchers(HttpMethod.POST, "/orders/complete").hasRole(roleNameUser)
                .antMatchers(HttpMethod.PUT, "/shopping-carts/movie-sessions")
                .hasRole(roleNameUser)
                .antMatchers(HttpMethod.GET, "/shopping-carts/by-user").hasRole(roleNameUser)
                .antMatchers(HttpMethod.GET, "/users/by-email").hasRole(roleNameAdmin)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
