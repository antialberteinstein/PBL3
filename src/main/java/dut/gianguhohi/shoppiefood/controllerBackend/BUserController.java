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
@RequestMapping("/api/user")
public class BUserController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity<?> createOrder(
        @RequestBody Order order,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            order.setCustomer(user);
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
        @PathVariable Integer orderId,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Order cancelledOrder = orderService.cancelOrder(orderId, user);
            return ResponseEntity.ok(cancelledOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            return ResponseEntity.ok(orderService.getOrdersByCustomer(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
        @PathVariable Integer orderId,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
