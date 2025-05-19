package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
public class BaseController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home") 
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            if (session.getAttribute("role").equals("user")) {
                return "redirect:/user/home";
            } else if (session.getAttribute("role").equals("shipper")) {
                return "redirect:/shipper/home";
            } else if (session.getAttribute("role").equals("restaurant")) {
                return "redirect:/restaurant/home";
            }
        }

        return "redirect:/auth/login";
    }

}
