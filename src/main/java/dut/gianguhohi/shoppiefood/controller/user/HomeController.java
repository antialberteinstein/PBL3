package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    
    @GetMapping("/user/home")
    public String home() {
        return "user/home";
    }
    
    @GetMapping("/user/order")
    public String orders() {
        return "user/order";
    }
} 