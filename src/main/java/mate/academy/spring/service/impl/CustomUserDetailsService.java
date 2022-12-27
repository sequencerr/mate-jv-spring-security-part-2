package mate.academy.spring.service.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import mate.academy.spring.model.User;
import mate.academy.spring.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userService.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Cannot find user with email=" + email));
        return withUsername(email)
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name()).toArray(String[]::new))
                .build();
    }
}
