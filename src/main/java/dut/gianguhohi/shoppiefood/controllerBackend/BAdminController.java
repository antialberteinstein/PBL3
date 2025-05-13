package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dut.gianguhohi.shoppiefood.models.Users.Admin;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class BAdminController {
    @Autowired
    private AdminRepository adminRepository;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestParam("loginName") String loginName,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        try {
            Admin admin = adminRepository.findByLoginName(loginName);
            if (admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid login credentials"));
            }
            session.setAttribute("admin", admin);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.removeAttribute("admin");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
