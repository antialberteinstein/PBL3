 package dut.gianguhohi.shoppiefood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.services.ShipperService;
import dut.gianguhohi.shoppiefood.services.OrderService;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Orders.Order;

@Controller
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/shipper/enter")
    public String enter(HttpSession session, Model model) {
        // session.setAttribute("role", "shipper");
        return "redirect:/shipper/home";
    }

    @GetMapping("/shipper/register")
    public String register(HttpSession session, Model model) {
        return "shipper/register";
    }

    @GetMapping("/shipper/exit")
    public String exit(HttpSession session) {
        // session.setAttribute("role", "user");
        return "redirect:/user/home";
    }

    @GetMapping("/shipper/home")
    public String home(HttpSession session, Model model) {

        // For testing purposes
        session.setAttribute("role", "shipper");

        return "shipper/home";
    }
}