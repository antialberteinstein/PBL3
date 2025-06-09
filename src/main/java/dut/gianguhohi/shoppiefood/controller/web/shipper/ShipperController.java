package dut.gianguhohi.shoppiefood.controller.web.shipper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }
        Integer userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            return "redirect:/auth/login";
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            return "redirect:/shipper/register";
        }

        session.setAttribute("shipperId", shipper.getShipperId());
        session.setAttribute("role", "shipper");

        return "redirect:/shipper/home";
    }

    // ==========================
    // == Register Shipper     ==
    // ==========================
    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }

        Integer userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            return "redirect:/auth/login";
        }
        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper != null) {
            return "redirect:/shipper/home";
        }
        return "shipper/register";
    }

    // ==========================
    // == Exit Shipper Mode    ==
    // ==========================
    @GetMapping("/exit")
    public String exit(HttpSession session) {
        session.setAttribute("role", "user");
        session.removeAttribute("shipperId");
        return "redirect:/user/home";
    }

    @PostMapping("/register")
    public String registerPost(
            HttpSession session,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("licensePlate") String plateNumber,
            @RequestParam("driverLicense") String driverLicense,
            Model model
    ) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        User user = userService.readById(userId);
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            shipperService.register(user, vehicleType, plateNumber, driverLicense);
            return "redirect:/shipper/enter";
        } catch (ResponseStatusException ex) {
            model.addAttribute("error", ex.getReason());
            return "shipper/register";
        }
    }
    

    // ==========================
    // == Shipper Home         ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Object shipperObj = session.getAttribute("shipperId");
        if (shipperObj == null) {
            return "redirect:/shipper/enter";
        }

        Integer shipperId = (Integer) shipperObj;
        Shipper shipper = shipperService.getShipperById(shipperId);

        if (shipper == null) {
            return "redirect:/shipper/register";
        }

        model.addAttribute("shipper", shipper);

        return "shipper/home";
    }

    @GetMapping("/profile")
    public String profile() {
        return "redirect:/shipper/home";
    }

    @GetMapping("/detail/{shipperId}")
    public String shipperDetail(@RequestParam("shipperId") Integer shipperId, Model model, HttpSession session) {
        Object shipperObj = session.getAttribute("shipperId");
        if (shipperObj == null) {
            return "redirect:/shipper/enter";
        }

        Shipper shipper = shipperService.getShipperById(shipperId);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy shipper");
        }

        model.addAttribute("shipper", shipper);
        return "shipper/home";
    }
}