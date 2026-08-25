package kashi.Demo.jobSeeker.dto;

import jakarta.persistence.ElementCollection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobUsersDto {
    Long id;
    String name;
    @ElementCollection
    private List<String> skills;
    String email;

    public JobUsersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
