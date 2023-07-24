package com.dailycodework.sbend2endapplication.registration.password;

import com.dailycodework.sbend2endapplication.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface PasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User theUser, String password);

    void savePasswordResetTokenForUser(User user, String passwordResetToken);
}
