package kashi.Demo.jobSeeker.security;

import kashi.Demo.jobSeeker.entity.UserLogin;
import kashi.Demo.jobSeeker.repository.LoginRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    // Constructor injection is highly recommended over field-level @Autowired for cleaner testing
    public CustomUserDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Use the updated relation traversal query method we set up earlier
        Optional<UserLogin> userOptional = loginRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        UserLogin loginUser = userOptional.get();

        // Safe extraction of the email string from the associated profile entity
        if (loginUser.getUserS() == null) {
            throw new UsernameNotFoundException("Authentication record has no connected profile mapping");
        }

        String userEmail = loginUser.getUserS().getEmail();

        // Build Spring Security's native UserDetails container
        return User.withUsername(userEmail)
                .password(loginUser.getPassword())
                .authorities("USER") // Required by newer Spring Security versions to prevent runtime errors
                .build();
    }
}
