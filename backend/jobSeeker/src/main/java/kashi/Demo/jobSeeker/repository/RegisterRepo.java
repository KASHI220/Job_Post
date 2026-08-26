package kashi.Demo.jobSeeker.repository;

import kashi.Demo.jobSeeker.entity.UserS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterRepo extends JpaRepository<UserS, Long> {
    Optional<UserS>findByUserSEmail(String email);
}
