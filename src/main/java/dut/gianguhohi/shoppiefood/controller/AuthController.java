package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
public class AuthController {
    

    @GetMapping("/auth/login")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register(Model model) {
        return "auth/register";
    }

    // logout
}
