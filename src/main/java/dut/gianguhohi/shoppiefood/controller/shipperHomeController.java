package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class shipperHomeController {
    @GetMapping("/shipper/home")
    public String shipperHome() {
        return "homepage_Shipper/home";
    }
}