 package dut.gianguhohi.shoppiefood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.services.ShipperService;


@Controller
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    // @Autowired
    // private OrderService orderService;

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
    public String home(Model model) {
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
     @GetMapping("/shipper/profile")
    public String profile(HttpSession session, Model model) {
        try {
            session.setAttribute("role", "shipper");

            // Thêm dữ liệu mẫu (có thể thay bằng dữ liệu thực từ database sau)
            model.addAttribute("shipperName", "PHAN MINH HIẾU");
            model.addAttribute("shipperId", "SH102230345");
            model.addAttribute("phone", "1234567890");
            model.addAttribute("email", "hieuphan@gmail.com");
            return "shipper/profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/shipper/home";
        }
    }
}