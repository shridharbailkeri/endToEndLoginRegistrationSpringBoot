package com.dailycodework.sbend2endapplication.user;

import com.dailycodework.sbend2endapplication.registration.RegistrationRequest;
import com.dailycodework.sbend2endapplication.registration.token.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //@RequiredArgsConstructor constructor based dependency injection at runtime
    // @RequiredArgsConstructor will inject all dependencies with final keyword
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenService verificationTokenService;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        var user = new User(registrationRequest.getFirstName(), registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findById(Long id) {

        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {
        userRepository.update(firstName, lastName, email, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        // we cant just delete use just like that
        // because user has relationship with other entities
        // example token
        // in User class there is no one to one relationship towards token
        // but in token class there is one to one relation ship to user - unidirectional relationship
        // means u can asccess user from token but not access token from the user
        // means we can say token.getUser but not user.getToken
        // so verification token became the parent and user became the child so we need to pass through the parent
        // we cant pass throught the child to delete the parent
        user.ifPresent(user1 -> verificationTokenService.deleteUserToken(user1.getId())); // if this operation fails (first)
        userRepository.deleteById(id); // this one will also fail (second one) that makes it transactional
    }
}
