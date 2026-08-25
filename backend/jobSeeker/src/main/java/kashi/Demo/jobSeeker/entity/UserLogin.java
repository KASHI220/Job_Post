package kashi.Demo.jobSeeker.entity;

import jakarta.persistence.*;


@Entity
public class    UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginID;
    private String password;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserS userS;

    public UserLogin() {
    }

    public UserS getUserS() {
        return userS;
    }

    public void setUserS(UserS userS) {
        this.userS = userS;
    }

    public Long getLoginID() {
        return loginID;
    }

    public void setLoginID(Long loginID) {
        this.loginID = loginID;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

