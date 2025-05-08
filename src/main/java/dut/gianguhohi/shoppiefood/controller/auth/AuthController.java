package dut.gianguhohi.shoppiefood.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.services.Users.UserService;
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
        Model model
    ) {
        try {
            User user = userService.readByPhoneNumber(phoneNumber);
            if (user == null) {
                user = userService.readByEmail(phoneNumber);
            }
            
            if (user == null || !user.getPassword().equals(password)) {
                model.addAttribute("message", "Sai số diện thoại, email hoặc mật khẩu");
                return "auth/login";
            }
            model.addAttribute("message", "Đăng nhập thành công");
            return "redirect:/user/home";
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
            userService.create(user, result);

            model.addAttribute("message", "Đăng ký thành công");
            return "redirect:/auth/login";
        } catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/register";
        }
    }
    
}
