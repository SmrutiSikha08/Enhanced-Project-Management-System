package com.udr.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.udr.entity.User;
import com.udr.myservice.MyServiceClass;

@Controller
public class MyController {

    @Autowired
    private MyServiceClass myServiceClass;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String ShowRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        myServiceClass.saveUser(user);
        model.addAttribute("success", true);
        return "redirect:/login?success=true";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "success", required = false) String success, Model model) {
        if ("true".equals(success)) {
            model.addAttribute("success", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model) {
        Optional<User> optionalUser = myServiceClass.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                model.addAttribute("user", user);
                return "dashboard"; // Forward to dashboard.html
            } else {
                model.addAttribute("error", "Invalid password");
            }
        } else {
            model.addAttribute("error", "User not found");
        }

        return "login";
    }

}
