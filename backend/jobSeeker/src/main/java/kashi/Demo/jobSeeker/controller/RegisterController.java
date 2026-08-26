package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.dto.RegisterDto;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class RegisterController {

    @Autowired
    RegisterService registerService;
    @PostMapping("/register")
    public UserS registerUser(@RequestBody RegisterDto registerDto){

        return registerService.registeredUser(registerDto);
    }

}
