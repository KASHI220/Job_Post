package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.JobUsersDto;
import kashi.Demo.jobSeeker.entity.UserLogin;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.LoginRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final LoginRepository loginRepository;

    public LoginService(AuthenticationManager authenticationManager,
                        LoginRepository loginRepository) {
        this.authenticationManager = authenticationManager;
        this.loginRepository = loginRepository;
    }

    public JobUsersDto login(UserLogin loginRequest) {

        // Safety check to ensure incoming JSON includes user information
        if (loginRequest.getUserS() == null || loginRequest.getUserS().getEmail() == null) {
            throw new RuntimeException("Email information is missing from request payload");
        }

        String email = loginRequest.getUserS().getEmail();

        // 1. Authenticate email and password using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
        );

        // 2. Fetch the credentials record from the database
        UserLogin userLoginRecord = loginRepository
                .findByUserSEmail(email)
                .orElseThrow(() -> new RuntimeException("User authentication profile not found"));

        // 3. Extract the underlying user profile entity
        UserS userProfile = userLoginRecord.getUserS();
        if (userProfile == null) {
            throw new RuntimeException("No user profile connected to this login account");
        }

        // 4. Convert the entity into your JobUsersDto to keep network traffic clean
        JobUsersDto dto = new JobUsersDto();
        dto.setId(userProfile.getUserId());
        dto.setName(userProfile.getName());
        dto.setEmail(userProfile.getEmail());
        dto.setSkills(userProfile.getSkills());

        return dto;
    }
}
