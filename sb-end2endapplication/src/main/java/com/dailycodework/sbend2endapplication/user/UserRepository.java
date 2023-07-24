package com.dailycodework.sbend2endapplication.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // we r not modifying everything example not password but modifying other fields only so u need @Modifying
    @Modifying
    @Query(value = "UPDATE User u set u.firstName =:firstName," +
            " u.lastName =:lastName, u.email =:email where u.id =:id")
    void update(String firstName, String lastName, String email, Long id);
}
