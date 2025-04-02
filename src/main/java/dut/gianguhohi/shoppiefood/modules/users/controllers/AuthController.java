package dut.gianguhohi.shoppiefood.modules.users.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import dut.gianguhohi.shoppiefood.modules.users.services.impl.UserService;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginResponse;
import dut.gianguhohi.shoppiefood.modules.users.dtos.CustomerRegisterRequest;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import dut.gianguhohi.shoppiefood.modules.users.dtos.RegisterResponse;  


@RestController
@RequestMapping("auth")
public class AuthController {
    
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public RedirectView showLoginPage() {
        return new RedirectView("/login.html");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse auth = userService.login(loginRequest);
            return ResponseEntity.ok(auth);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/register/customer")
    public RedirectView showRegisterPageForCustomer() {
        return new RedirectView("/customer_register.html");
    }

    @PostMapping("/register/customer")
    public ResponseEntity<RegisterResponse> registerCustomer(@RequestBody CustomerRegisterRequest registerRequest) {
        try {
            RegisterResponse auth = userService.register(registerRequest);
            return ResponseEntity.ok(auth);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
