package dut.gianguhohi.shoppiefood.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class BaseController {

    // ==========================
    // == Index Redirect       ==
    // ==========================
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        return "redirect:/home";
    }

    // ==========================
    // == Home Redirect        ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role != null) {
            switch (role) {
                case "user":
                    return "redirect:/user/home";
                case "shipper":
                    return "redirect:/shipper/home";
                case "restaurant":
                    return "redirect:/restaurant/home";
                default:
                    break;
            }
        }
        return "redirect:/auth/login";
    }
}