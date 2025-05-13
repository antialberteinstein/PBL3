package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import dut.gianguhohi.shoppiefood.models.Users.Admin;

@Controller
public class AdminController {
    
    @GetMapping("/admin/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("admin") != null) {
            return "redirect:/admin/home";
        }
        return "admin/login";
    }

    @GetMapping("/admin/home")
    public String home(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin/home";
    }
} 