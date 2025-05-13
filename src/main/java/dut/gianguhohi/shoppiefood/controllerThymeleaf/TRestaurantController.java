package dut.gianguhohi.shoppiefood.controllerThymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
public class TRestaurantController {
    
    @GetMapping("/restaurant/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("restaurant")) {
            return "redirect:/restaurant/enter";
        }
        model.addAttribute("user", user);
        return "restaurant/home";
    }

    @GetMapping("/restaurant/enter")
    public String enter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        if (session.getAttribute("role") != null && session.getAttribute("role").equals("restaurant")) {
            return "redirect:/restaurant/home";
        }
        return "restaurant/enter";
    }

    @GetMapping("/restaurant/exit")
    public String exit(HttpSession session) {
        session.removeAttribute("role");
        return "redirect:/user/home";
    }
} 