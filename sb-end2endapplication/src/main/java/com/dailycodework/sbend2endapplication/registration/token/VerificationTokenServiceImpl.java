package com.dailycodework.sbend2endapplication.registration.token;

import com.dailycodework.sbend2endapplication.user.User;
import com.dailycodework.sbend2endapplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    @Override
    public String validateToken(String token) {
        Optional<VerificationToken> theToken = tokenRepository.findByToken(token);
        if (theToken.isEmpty()) {
            return "INVALID";
        }
        // get user from token
        User user = theToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if ((theToken.get().getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return "EXPIRED";
        }
        user.setEnabled(true);
        // then save user again
        userRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        var verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        tokenRepository.deleteByUserId(id);
    }
}
