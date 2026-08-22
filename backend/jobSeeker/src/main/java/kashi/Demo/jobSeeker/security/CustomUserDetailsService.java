package kashi.Demo.jobSeeker.security;

import kashi.Demo.jobSeeker.entity.UserLogin;
import kashi.Demo.jobSeeker.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    LoginRepository loginRopository;
    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<UserLogin> user = loginRopository.findByStEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User not found: " + username
            );
        }

        UserLogin loginUser = user.get();

        return User.withUsername(loginUser.getEmail())
                .password(loginUser.getPassword())
                .build();

    }
}