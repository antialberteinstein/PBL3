package dut.gianguhohi.shoppiefood.controllers.Users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final String LOGIN_PAGE = "Users/login";
    private static final String REGISTER_PAGE = "Users/register"; 

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "Welcome to Login Page");
        return LOGIN_PAGE;
    }

    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("message", "Welcome to Register Page");
        return REGISTER_PAGE;
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        Model model
    ) {
        if (username.equals("admin") && password.equals("123456")) {
            model.addAttribute("message", "Login successful");
            return LOGIN_PAGE;
        }
        model.addAttribute("message", "Login failed");
        return LOGIN_PAGE;
    }

    @PostMapping("/register")
    public String register(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        return "";
    }
}
