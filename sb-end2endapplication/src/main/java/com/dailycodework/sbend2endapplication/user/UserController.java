package com.dailycodework.sbend2endapplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
// this controller is not rest controller, as we are creating web mvc that is the front end to actually use our api
// thats why instead of using rest controller we use normal @Controller annotation
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    //https://github.com/dailycodework/spring-mvc-web-app-with-thymeleaf

    private final UserService userService;

    @GetMapping
    public String getUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user) {
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/users?update_success";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
    }
}
