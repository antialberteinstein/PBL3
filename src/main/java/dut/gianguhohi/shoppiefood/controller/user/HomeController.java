package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/user/home")
    public String home() {
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        return "user/home";
    }
} 