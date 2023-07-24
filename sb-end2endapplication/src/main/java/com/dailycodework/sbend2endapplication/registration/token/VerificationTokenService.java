package com.dailycodework.sbend2endapplication.registration.token;

import com.dailycodework.sbend2endapplication.user.User;

import java.util.Optional;

public interface VerificationTokenService {

    String validateToken(String token);

    void saveVerificationTokenForUser(User user, String token);

    Optional<VerificationToken> findByToken(String token);

    void deleteUserToken(Long id);
}
