package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import java.util.Map;

@RestController
@RequestMapping("/api/discount")
public class BDiscountController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyDiscount(
        @RequestParam("orderId") Integer orderId,
        @RequestParam("discountCode") String discountCode,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Order order = orderService.applyDiscount(orderId, discountCode);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
