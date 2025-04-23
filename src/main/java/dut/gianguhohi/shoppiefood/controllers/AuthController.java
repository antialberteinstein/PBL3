package dut.gianguhohi.shoppiefood.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "Welcome to Login Page");
        return "login";
    }

    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("message", "Welcome to Register Page");
        return "register";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        Model model
    ) {
        if (username.equals("admin") && password.equals("123456")) {
            model.addAttribute("message", "Login successful");
            return "login";
        }
        model.addAttribute("message", "Login failed");
        return "login";
    }
}
