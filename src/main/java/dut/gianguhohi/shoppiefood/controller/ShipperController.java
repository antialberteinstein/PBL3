 package dut.gianguhohi.shoppiefood.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
        /* if (session.getAttribute("role") != null && session.getAttribute("role").equals("shipper")) {
            return "redirect:/shipper/home";
        } */

        try {
            // User user = (User) session.getAttribute("user");
            /* if (user == null) {
                return "redirect:/auth/login";
            }
            Shipper shipper = shipperService.getShipperByUser(user);
            if (shipper == null) {
                return "redirect:/shipper/register";
            } */

            session.setAttribute("role", "shipper");
            return "redirect:/shipper/home";
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/shipper/register")
    public String register(HttpSession session, Model model) {
        if (session.getAttribute("role") != null && session.getAttribute("role").equals("shipper")) {
            return "redirect:/shipper/home";
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper != null) {
            return "redirect:/shipper/home";
        }
        return "shipper/register";
    }

    @GetMapping("/shipper/exit")
    public String exit(HttpSession session) {
        session.removeAttribute("role");
        return "redirect:/user/home";
    }

    @GetMapping("/shipper/home")
    public String home(Model model, Authentication authentication) {
        try {
            // User user = (User) session.getAttribute("user");
            /* if (user == null) {
                return "redirect:/auth/login";
            } */
            // Shipper shipper = shipperService.getShipperByUser(user);
            /* if (shipper == null) {
                return "redirect:/shipper/register";
            } */
            /* List<Order> orders = orderService.getOrdersByShipper(shipper);
            model.addAttribute("orders", orders);
            model.addAttribute("shipper", shipper); */
            model.addAttribute("userRole", "SHIPPER");
            return "shipper/home";
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }
}