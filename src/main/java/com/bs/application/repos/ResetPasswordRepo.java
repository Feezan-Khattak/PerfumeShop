package com.bs.application.repos;

import com.bs.application.entities.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPassword, Long> {
    Optional<ResetPassword> findByToken(String token);
}
