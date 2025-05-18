package dut.gianguhohi.shoppiefood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.services.OrderService;


@Controller
public class UserController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/home")
    public String home(Model model, HttpSession session) {
        
        // For testing purposes
        session.setAttribute("role", "user");

        return "user/home";
    }

    @GetMapping("/user/order")
    public String userOrders(HttpSession session, Model model) {
        return "user/order";
    }
} 