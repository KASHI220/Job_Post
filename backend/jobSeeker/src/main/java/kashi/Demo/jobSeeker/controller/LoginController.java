package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.dto.JobUsersDto;
import kashi.Demo.jobSeeker.dto.UserLoginDto;
import kashi.Demo.jobSeeker.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<JobUsersDto> handleLogin(@Validated @RequestBody UserLoginDto loginRequest) {
        JobUsersDto authenticatedUser = loginService.login(loginRequest);
        return ResponseEntity.ok(authenticatedUser);
    }
}
