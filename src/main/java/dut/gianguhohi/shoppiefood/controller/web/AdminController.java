package dut.gianguhohi.shoppiefood.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // ==========================
    // == Admin Login          ==
    // ==========================
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/home";
        }
        return "admin/login";
    }

    // ==========================
    // == Admin Home           ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Integer adminId = (Integer) session.getAttribute("adminId");
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("adminId", adminId);
        return "admin/home";
    }

    // ==========================
    // == Admin Logout         ==
    // ==========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("adminId");
        return "redirect:/admin/login";
    }
}