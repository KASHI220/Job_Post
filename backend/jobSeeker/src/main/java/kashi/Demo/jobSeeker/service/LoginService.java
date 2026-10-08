package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.JobUsersDto;
import kashi.Demo.jobSeeker.dto.UserLoginDto;
import kashi.Demo.jobSeeker.entity.Job;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.RegisterRepo;
import kashi.Demo.jobSeeker.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final RegisterRepo registerRepo;


    public LoginService(AuthenticationManager authenticationManager, RegisterRepo registerRepo) {
        this.authenticationManager = authenticationManager;
        this.registerRepo = registerRepo;
    }
    @Autowired
    SearchRepository searchRepository;

    public List<Job> login(UserLoginDto loginRequest) {


        if (loginRequest.getPassword() == null || loginRequest.getEmail() == null) {
            throw new RuntimeException("Email and password credentials are missing from request payload");
        }

        String email = loginRequest.getEmail();


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
        );


        UserS userProfile = registerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User authentication profile not found"));

//        JobUsersDto dto = new JobUsersDto();
//        dto.setId(userProfile.getUserId());
//        dto.setName(userProfile.getName());
//        dto.setEmail(userProfile.getEmail());
       List<String> list =  userProfile.getSkills();
       StringBuilder st = new StringBuilder();

       for( String s : list){
           st.append(s);
           st.append(" ");
       }
       return searchRepository.searchByText(st.toString());

    }
}
