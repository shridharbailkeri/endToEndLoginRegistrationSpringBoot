package com.dailycodework.sbend2endapplication.registration.password;

import com.dailycodework.sbend2endapplication.user.User;
import com.dailycodework.sbend2endapplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public String validatePasswordResetToken(String theToken) {

        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(theToken);

        if (passwordResetToken.isEmpty()) {
            return "invalid";
        }
        Calendar calendar = Calendar.getInstance();

        if ((passwordResetToken.get().getExpitationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }

    @Override
    public void savePasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetToken passwordResetToken1 = new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(passwordResetToken1);
    }
}
