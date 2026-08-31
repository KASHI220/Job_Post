package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.RegisterDto;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.RegisterRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final RegisterRepo registerRepo;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(RegisterRepo registerRepo,
                           PasswordEncoder passwordEncoder) {
        this.registerRepo = registerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserS registeredUser(RegisterDto registerDto) {

        UserS userS = new UserS();

        userS.setName(registerDto.getName());
        userS.setEmail(registerDto.getEmail());

        // Encode password before storing it
        userS.setPassword(
                passwordEncoder.encode(registerDto.getPassword())
        );

        userS.setSkills(registerDto.getSkills());

        return registerRepo.save(userS);
    }
}