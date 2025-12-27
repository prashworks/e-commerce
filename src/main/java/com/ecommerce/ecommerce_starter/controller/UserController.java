package com.ecommerce.ecommerce_starter.controller;

import com.ecommerce.ecommerce_starter.model.Role;
import com.ecommerce.ecommerce_starter.model.User;
import com.ecommerce.ecommerce_starter.repository.RoleRepository;
import com.ecommerce.ecommerce_starter.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Controller
public class UserController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public UserController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(Principal principal) {
        if (principal != null) {
            return "redirect:/products";
        }
        return "login";
    }
}