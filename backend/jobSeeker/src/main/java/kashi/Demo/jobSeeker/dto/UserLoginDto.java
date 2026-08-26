package kashi.Demo.jobSeeker.dto;

public class UserLoginDto {


    private String password;
    private String email;


    public UserLoginDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}