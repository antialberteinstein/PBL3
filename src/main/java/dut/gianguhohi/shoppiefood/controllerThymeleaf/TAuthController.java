package dut.gianguhohi.shoppiefood.controllerThymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
public class TAuthController {
    
    @GetMapping("/auth/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/user/home";
        }
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/user/home";
        }
        return "auth/register";
    }

    @GetMapping("/user/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        return "auth/profile";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/auth/login";
    }
}
