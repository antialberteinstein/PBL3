package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/customer/home")
    public String home() {
        return "homepage_Customer/home";
    }

    @GetMapping("/customer/cart")
    public String cart() {
        return "homepage_Customer/cart";
    }

    @GetMapping("/customer/order")
    public String orders() {
        return "homepage_Customer/order";
    }
} 