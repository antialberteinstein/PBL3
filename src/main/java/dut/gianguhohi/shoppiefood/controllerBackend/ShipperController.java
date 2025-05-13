package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.services.ShipperService;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.services.OrderService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/shipper/register")
    @ResponseBody
    public Shipper register(
        @RequestParam("vehicleType") String vehicleType,
        @RequestParam("licensePlate") String licensePlate,
        @RequestParam("driverLicense") String driverLicense,
        HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new RuntimeException("User not logged in");
            }
            Shipper shipper = shipperService.getShipperByUser(user);
            if (shipper != null) {
                throw new RuntimeException("User already registered as shipper");
            }

            return shipperService.register(user, vehicleType, licensePlate, driverLicense);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}