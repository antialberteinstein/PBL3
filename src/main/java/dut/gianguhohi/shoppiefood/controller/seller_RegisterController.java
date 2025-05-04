package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class seller_RegisterController {
    @GetMapping("/seller_register")
    public String seller_register() {
        return "Login_Register/seller_register";
    }
}