package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.services.UserService;
import dut.gianguhohi.shoppiefood.models.Users.User;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class BAuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestParam("phoneNumber") String phoneNumber,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        try {
            User user = userService.login(phoneNumber, password);
            session.setAttribute("user", user);
            return ResponseEntity.ok(user);
        } catch (UserService.LoginRelatedException | UserService.UserRelatedException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody User user,
        BindingResult result
    ) {
        try {
            User registeredUser = userService.register(user, result);
            return ResponseEntity.ok(registeredUser);
        } catch (UserService.UserExistsException | UserService.RegisterRelatedException | UserService.UserRelatedException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.removeAttribute("user");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
