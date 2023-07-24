package com.dailycodework.sbend2endapplication.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByToken(String token);

    void deleteByUserId(Long id);
}
