package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.UserService;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register(Model model) {
        return "auth/register";
    }

    @GetMapping("/auth/logout")
    public String logout(Model model, HttpSession session) {
        // Clear the session
        session.invalidate();
        // Redirect to the login page
        return "redirect:/auth/login";
    } 

    @PostMapping("/auth/login")
    public String login(
        @RequestParam("loginString") String loginString,
        @RequestParam("password") String password,
        Model model,
        HttpSession session
    ) {
        try {
            User user = userService.login(loginString, password);
            if (user != null) {
                session.setAttribute("role", "user");
                session.setAttribute("user", user);
                return "redirect:/user/home";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/auth/register")
    public String register(
        @RequestParam("phoneNumber") String phoneNumber,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam("name") String name,
        @RequestParam("dateOfBirth") String dateOfBirth,
        @RequestParam("gender") String gender,
        Model model
    ) {
        try {
            userService.register(phoneNumber, email, password, confirmPassword, name, dateOfBirth, gender);
            model.addAttribute("success", "Đăng ký thành công, vui lòng đăng nhập để tiếp tục");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
