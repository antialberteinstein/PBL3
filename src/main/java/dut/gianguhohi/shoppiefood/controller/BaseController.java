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
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (session.getAttribute("role") != null && session.getAttribute("role").equals("shipper")) {
                return "redirect:/shipper/home";
            }
            return "redirect:/user/home";
        }
        return "redirect:/auth/login";
    }

}
