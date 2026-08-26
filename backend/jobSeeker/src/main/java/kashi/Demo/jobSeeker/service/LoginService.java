package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.JobUsersDto;
import kashi.Demo.jobSeeker.dto.UserLoginDto;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.RegisterRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final RegisterRepo registerRepo; // Marked as private for proper encapsulation

    public LoginService(AuthenticationManager authenticationManager, RegisterRepo registerRepo) {
        this.authenticationManager = authenticationManager;
        this.registerRepo = registerRepo;
    }

    public JobUsersDto login(UserLoginDto loginRequest) {

        // 1. Safety check to ensure incoming JSON includes required credentials
        if (loginRequest.getPassword() == null || loginRequest.getEmail() == null) {
            throw new RuntimeException("Email and password credentials are missing from request payload");
        }

        String email = loginRequest.getEmail();

        // 2. Perform standard authentication checks using your CustomUserDetailsService
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
        );

        // 3. Fetch the record from the database using the instance variable and correct method name
        UserS userProfile = registerRepo.findByUserSEmail(email)
                .orElseThrow(() -> new RuntimeException("User authentication profile not found"));

        // 4. Convert the entity directly into your JobUsersDto (No nested mapping needed)
        JobUsersDto dto = new JobUsersDto();
        dto.setId(userProfile.getUserId());
        dto.setName(userProfile.getName());
        dto.setEmail(userProfile.getEmail());
        dto.setSkills(userProfile.getSkills());

        return dto;
    }
}
