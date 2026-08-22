package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.entity.UserLogin;
import kashi.Demo.jobSeeker.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLogin> login (@RequestBody @Validated UserLogin loginRequest) {
        Student student =loginService.login(loginRequest);
        return ResponseEntity.ok();
    }
}
