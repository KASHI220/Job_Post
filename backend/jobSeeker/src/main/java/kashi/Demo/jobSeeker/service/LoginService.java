package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.entity.UserLogin;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final LoginRopository loginRopository;
    private final StudentRepository studentRepository;

    public LoginService(AuthenticationManager authenticationManager,
                        LoginRopository loginRopository,
                        StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.loginRopository = loginRopository;
        this.studentRepository = studentRepository;
    }

    public Student login(UserLogin loginRequest) {

        // 1. Authenticate email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),loginRequest.getPassword()
                )
        );

        // 2. Find the UserLogin from database
        UserLogin user = loginRopository
                .findByStEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 3. Find the corresponding Student
        return studentRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );
    }
}
