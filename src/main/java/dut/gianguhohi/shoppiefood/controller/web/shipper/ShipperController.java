package dut.gianguhohi.shoppiefood.controller.web.shipper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.services.ShipperService;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.user.UserService;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ==========================
    // == Enter Shipper Mode   ==
    // ==========================
    @GetMapping("/enter")
    public String enter(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        User user = userService.readById(userId);
        if (user == null) {
            return "redirect:/auth/login";
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            return "redirect:/shipper/register";
        }

        session.setAttribute("shipper", shipper);
        session.setAttribute("role", "shipper");

        return "redirect:/shipper/home";
    }

    // ==========================
    // == Register Shipper     ==
    // ==========================
    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        return "shipper/register";
    }

    // ==========================
    // == Exit Shipper Mode    ==
    // ==========================
    @GetMapping("/exit")
    public String exit(HttpSession session) {
        session.setAttribute("role", "user");
        session.removeAttribute("shipper");
        return "redirect:/user/home";
    }

    // ==========================
    // == Shipper Home         ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("shipper") == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipper không tồn tại");
        } 
        return "shipper/home";
    }
}