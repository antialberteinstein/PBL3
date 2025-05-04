package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Logincontroller {
    @GetMapping("/login")
    public String login() {
        return "Login_Register/login";
    }
}
