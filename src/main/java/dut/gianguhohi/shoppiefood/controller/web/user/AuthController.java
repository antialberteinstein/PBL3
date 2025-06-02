package dut.gianguhohi.shoppiefood.controller.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.user.AuthService;
import dut.gianguhohi.shoppiefood.services.user.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "auth/register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("loginString") String loginString,
        @RequestParam("password") String password,
        Model model,
        HttpSession session
    ) {
        try {
            User user = authService.login(loginString, password);
            if (user != null) {
                session.setAttribute("role", "user");
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
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

    @PostMapping("/register")
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