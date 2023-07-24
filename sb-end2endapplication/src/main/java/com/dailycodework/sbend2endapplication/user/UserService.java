package com.dailycodework.sbend2endapplication.user;

import com.dailycodework.sbend2endapplication.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User registerUser(RegistrationRequest registrationRequest);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);
}
