package kashi.Demo.jobSeeker.dto;

import org.springframework.stereotype.Component;

@Component
public class JobUsersDto {
    Long id;
    String name;
    String [] skills;
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

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
