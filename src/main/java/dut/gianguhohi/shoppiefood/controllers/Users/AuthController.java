package dut.gianguhohi.shoppiefood.controllers.Users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import dut.gianguhohi.shoppiefood.services.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;
import dut.gianguhohi.shoppiefood.models.Users.Admin;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @Autowired
    private AdminRepository adminRepository;

    private static final String LOGIN_PAGE = "Users/login";
    private static final String ADMIN_LOGIN_PAGE = "Users/admin_login";
    private static final String REGISTER_PAGE = "Users/register"; 
    private static final String SELLER_REGISTER_PAGE = "Users/seller_register";
    private static final String SELLER_LOGIN_PAGE = "Users/seller_login";

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "Welcome to Login Page");
        return LOGIN_PAGE;
    }

    @GetMapping("/admin/login")
    public String adminLogin(Model model) {
        model.addAttribute("message", "Welcome to Admin Login Page");
        return ADMIN_LOGIN_PAGE;
    }

    @PostMapping("/admin/login")
    public String adminLogin(
        @RequestParam("loginName") String loginName,
        @RequestParam("password") String password,
        Model model
    ) {
        try {
            Admin admin = adminRepository.findByLoginName(loginName);
            if (admin == null || !admin.getPassword().equals(password)) {
                model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu");
                return ADMIN_LOGIN_PAGE;
            }
            model.addAttribute("message", "Đăng nhập thành công");
            return ADMIN_LOGIN_PAGE;
        } catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());
            return ADMIN_LOGIN_PAGE;
        }
    }
    

    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("message", "Welcome to Register Page");
        return REGISTER_PAGE;
    }

    @PostMapping("/login")
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
                return LOGIN_PAGE;
            }
            model.addAttribute("message", "Đăng nhập thành công");
            return LOGIN_PAGE;
        } catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());

            return LOGIN_PAGE;
        }
    }

    @PostMapping("/register")
    public String register(
        @ModelAttribute("user") User user,
        BindingResult result,
        Model model
    ) {
        try {
            userService.create(user, result);
            model.addAttribute("message", "Đăng ký thành công");
            return REGISTER_PAGE;
        } catch (UserService.UserRelatedException e) {
            model.addAttribute("message", e.getMessage());
        }
        return REGISTER_PAGE;
    }
}
