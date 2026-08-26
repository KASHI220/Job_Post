package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.RegisterDto;
import kashi.Demo.jobSeeker.entity.UserS;
import kashi.Demo.jobSeeker.repository.RegisterRepo;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    final RegisterRepo registerRepo;

   public RegisterService(RegisterRepo registerRepo) {
       this.registerRepo= registerRepo;

   }
   public UserS registeredUser(RegisterDto registerDto){
       UserS userS = new UserS();
       userS.setName(registerDto.getName());
       userS.setEmail(registerDto.getEmail());
       userS.setPassword(registerDto.getPassword());
       userS.setSkills(registerDto.getSkills());

      return registerRepo.save(userS);

   }
}
