package com.dailycodework.sbend2endapplication.registration;

import com.dailycodework.sbend2endapplication.event.RegistrationCompleteEvent;
import com.dailycodework.sbend2endapplication.event.listener.RegistrationCompleteEventListener;
import com.dailycodework.sbend2endapplication.registration.password.PasswordResetTokenService;
import com.dailycodework.sbend2endapplication.registration.token.VerificationToken;
import com.dailycodework.sbend2endapplication.registration.token.VerificationTokenRepository;
import com.dailycodework.sbend2endapplication.registration.token.VerificationTokenService;
import com.dailycodework.sbend2endapplication.user.User;
import com.dailycodework.sbend2endapplication.user.UserService;
import com.dailycodework.sbend2endapplication.utility.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    //https://github.com/dailycodework/spring-mvc-web-app-with-thymeleaf
    //http://localhost:8080/registration/registration-form

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final VerificationTokenService tokenService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final RegistrationCompleteEventListener eventListener;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register") // th:object="${user} in thymleaf comes from @ModelAttribute("user")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest, HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        // publish the verification email event  here
        // when user is saved in database we publish the event
        // when the below event is published the listener RegistrationCompleteEventListener is listening to any event that is published
        // so when an event is published so listener will fire the email , getting the user and getting users email
        publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        // we have published the event, but now we need listener thats going to send the email
        return "redirect:/registration/registration-form?success";
    }

    // we set token as param remember ? its a request param  in listener
    // String url = event.getConfirmationUrl() + "/registration/verifyEmail?token"+vToken;
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        // if user is verified we send message to user that this email has been verified
        // if the token is appended in the link and then we check if the user in that token has been enabled?
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }

        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model){ // we need to request name="email" from front end here
        String email = request.getParameter("email"); // this is the email that user has entered in the form
        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/registration/forgot-password-request?not_found";
        }
        // if user is found then create new token
        String passwordResetToken = UUID.randomUUID().toString();

        passwordResetTokenService.savePasswordResetTokenForUser(user, passwordResetToken);

        // send password reset verification email to user
        String url = UrlUtil.getApplicationUrl(request) + "/registration/password-reset-form?token="+passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            //throw new RuntimeException(e); no need to throw this here as we r trying to return this to our form
            model.addAttribute("error", e.getMessage());
        }

        // is email sending is successfull
        return "redirect:/registration/forgot-password-request?success";

    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request) {
        String theToken = request.getParameter("token");  // corresponds to name="token"  in front end form ,
        // token helps to track for which user we r changing the password
        String password = request.getParameter("password"); // corresponds to name="password" in front end form

        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);

        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "redirect:/error?invalid_token";
        }

        // or extract user from the token and check him in database
        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);

        if (theUser.isPresent()) {
            passwordResetTokenService.resetPassword(theUser.get(), password);

            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";

    }

}
