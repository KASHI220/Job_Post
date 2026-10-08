package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.dto.UserLoginDto;
import kashi.Demo.jobSeeker.entity.Job;
import kashi.Demo.jobSeeker.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<List<Job>> handleLogin(
            @RequestBody UserLoginDto loginRequest) {

        List<Job> recommendedJobs = loginService.login(loginRequest);

        return ResponseEntity.ok(recommendedJobs);
    }
}
