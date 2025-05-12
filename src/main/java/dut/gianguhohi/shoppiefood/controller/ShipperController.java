package dut.gianguhohi.shoppiefood.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.service.OrderService;
import dut.gianguhohi.shoppiefood.model.Order;
import java.util.List;
import javax.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.model.User;
import dut.gianguhohi.shoppiefood.model.Shipper;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ShipperController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/shipper/home")
    public String home(HttpSession session,Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/auth/login";
            }
            try {
                Shipper shipper = (Shipper) session.getAttribute("shipper");
                List<Order> orders = orderService.getOrdersByShipper(shipper);
                model.addAttribute("orders", orders);

                return "shipper/home";
            } catch (Exception e) {
                return "redirect:/shipper/register";
            }
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/shipper/register")
    public String register(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        if (session.getAttribute("shipper") != null) {
            return "redirect:/shipper/home";
        }
        return "auth/shipper_register";
    }
}