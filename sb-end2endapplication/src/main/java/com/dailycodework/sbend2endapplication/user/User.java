package com.dailycodework.sbend2endapplication.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //th:field="@{firstName}" inn thymleaf form should be same as pojo attribute
    private String firstName;

    private String lastName;

    // by default is false, but user needs to change it if he/she does mistake, so mutable true
    @NaturalId(mutable = true)
    private String email;

    private String password;

    private boolean isEnabled = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // id mentioned above in User class / in this class is referenced here -> in referencedColumnName = "id"
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) // here referencedColumnName = "id" refers to id column in row entity

    private Collection<Role> roles;


    public User(String firstName, String lastName, String email,
                String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
