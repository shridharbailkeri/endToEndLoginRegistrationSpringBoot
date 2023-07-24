package com.dailycodework.sbend2endapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class EndToEndSecurityDemo {

    // This method is annotated with @Bean,
    // indicating that it is a configuration method for creating a bean
    // It creates and configures an instance of the PasswordEncoder interface
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/", "/login", "/error","/registration/**", "/users")
                .permitAll()
                // because we r creating custom login page we specify login page here
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                // if login is successfull go to homepage  .defaultSuccessUrl("/")
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                // if user wants to logout then clear session
                .permitAll().and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                // then we specify the url that handles logout, if logout is successful we return to homepage .logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
                .build();
    }
}
