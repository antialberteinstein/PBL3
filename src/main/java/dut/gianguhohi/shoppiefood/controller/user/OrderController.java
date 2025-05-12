package dut.gianguhohi.shoppiefood.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.service.OrderService;
import dut.gianguhohi.shoppiefood.model.Order;
import org.springframework.ui.Model;
import java.util.List;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/orders")
    public String userOrders(HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/auth/login";
            }

            List<Order> orders = orderService.getOrdersByCustomer(user);
            model.addAttribute("orders", orders);
            return "user/order";
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }
}
