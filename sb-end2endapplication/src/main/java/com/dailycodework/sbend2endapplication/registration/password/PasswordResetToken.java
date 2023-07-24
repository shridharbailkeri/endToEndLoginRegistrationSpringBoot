package com.dailycodework.sbend2endapplication.registration.password;

import com.dailycodework.sbend2endapplication.registration.token.TokenExpirationTime;
import com.dailycodework.sbend2endapplication.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expitationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expitationTime = TokenExpirationTime.getExpirationTime();
    }


}
