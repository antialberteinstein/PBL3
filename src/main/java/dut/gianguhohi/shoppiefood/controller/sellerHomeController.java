package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class sellerHomeController {
    @GetMapping("/seller/home")
    public String sellerHome() {
        return "homepage_Seller/home";
    }
}