package dut.gianguhohi.shoppiefood.controller.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/home")
    public String home(Model model) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }
        Integer userId = (Integer) userIdObj;
        if (userId == null) {
            return "redirect:/auth/login";
        }
        if (userId != null) {
            User user = userService.readById(userId);
            model.addAttribute("user", user);
        }

        return "user/home";
    }

    @GetMapping("/order")
    public String userOrders(Model model) {
        // You can fetch userId and orders here if needed
        return "user/order";
    }
}