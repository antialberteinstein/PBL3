package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class shipper_RegisterController {
    @GetMapping("/shipper_register")
    public String shipper_register() {
        return "Login_Register/shipper_register";
    }
}