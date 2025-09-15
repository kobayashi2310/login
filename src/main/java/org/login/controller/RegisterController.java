package org.login.controller;

import lombok.RequiredArgsConstructor;
import org.login.model.User;
import org.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String registerPost(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {

        User user = new User(null, name, passwordEncoder.encode(password), User.Role.USER);
        userRepository.save(user);

        return "redirect:/login";

    }

}
