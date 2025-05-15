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

@Controller
public class UserController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/home")
    public String home(HttpSession session) {
        /* if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        } */
        return "user/home";
    }

    @GetMapping("/user/order")
    public String userOrders(HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("user");
            /* if (user == null) {
                return "redirect:/auth/login";
            } */

            List<Order> orders = orderService.getOrdersByCustomer(user);
            model.addAttribute("orders", orders);
            return "user/order";
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }
} 