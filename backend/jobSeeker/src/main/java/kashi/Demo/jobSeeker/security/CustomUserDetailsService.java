package kashi.Demo.jobSeeker.security;

import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.RegisterRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RegisterRepo registerRepo;

    public CustomUserDetailsService(RegisterRepo registerRepo) {
        this.registerRepo = registerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserS user = registerRepo.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + username
                        ));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}