package kashi.Demo.jobSeeker.repository;

import kashi.Demo.jobSeeker.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface LoginRepository extends JpaRepository<UserLogin,Long> {
    Optional<UserLogin> findByEmail(String stEmail);
}
