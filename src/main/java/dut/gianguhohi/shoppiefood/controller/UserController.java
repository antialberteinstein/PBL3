package dut.gianguhohi.shoppiefood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import java.util.List;
import java.util.ArrayList;
import dut.gianguhohi.shoppiefood.models.Product.Product;

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