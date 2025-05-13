package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/user/order/create")
    @ResponseBody
    public Order createOrder(@RequestBody Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        order.setCustomer(user);
        return orderService.createOrder(order);
    }

    @PostMapping("/user/order/cancel")
    @ResponseBody
    public Order cancelOrder(@RequestParam Long orderId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        return orderService.cancelOrder(orderId, user);
    }
}
