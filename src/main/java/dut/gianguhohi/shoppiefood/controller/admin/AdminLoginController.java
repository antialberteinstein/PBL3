package dut.gianguhohi.shoppiefood.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import dut.gianguhohi.shoppiefood.models.Users.Admin;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;


@Controller
public class AdminLoginController {
    @Autowired
    public AdminRepository adminRepository;
    
    @GetMapping("/admin/login")
    public String login(Model model) {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(
        @RequestParam("loginName") String loginName,
        @RequestParam("password") String password,
        Model model
    ) {
        Admin admin = adminRepository.findByLoginName(loginName);
        if (admin == null || !admin.getPassword().equals(password)) {
            model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu");
            return "admin/login";
        }
        model.addAttribute("message", "Đăng nhập thành công");
        return "redirect:/admin/home";
    }
}
