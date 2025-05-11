package dut.gianguhohi.shoppiefood.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.services.Users.UserService;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import dut.gianguhohi.shoppiefood.models.Users.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepository adminRepository;


    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "auth/register";
    }
    
    @PostMapping("/auth/login")
    public String login(
        @RequestParam("phoneNumber") String phoneNumber,
        @RequestParam("password") String password,
        Model model,
        HttpSession session
    ) {
        try {
            User user = userService.login(phoneNumber, password);
            session.setAttribute("user", user);
            return "redirect:/user/home";
        } catch (UserService.LoginRelatedException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/login";
        } catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/auth/register")
    public String register(
        @ModelAttribute("user") User user,
        BindingResult result,
        Model model
    ) {
        try {
            userService.register(user, result);
            return "redirect:/auth/login";
        } catch (UserService.UserExistsException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/register";
        } catch (UserService.RegisterRelatedException e) {
            model.addAttribute("message", "Có lỗi xảy ra trong quá trình đăng ký");
            return "auth/register";
        }
        catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/register";
        }
    }
    
}
