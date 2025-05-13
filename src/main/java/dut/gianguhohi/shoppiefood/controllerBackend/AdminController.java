package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import dut.gianguhohi.shoppiefood.models.Users.Admin;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    public AdminRepository adminRepository;
    
    @PostMapping("/admin/login")
    @ResponseBody
    public Admin login(
        @RequestParam("loginName") String loginName,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        Admin admin = adminRepository.findByLoginName(loginName);
        if (admin == null || !admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid login credentials");
        }
        session.setAttribute("admin", admin);
        return admin;
    }
}
