package dut.gianguhohi.shoppiefood.controller.rest.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/user")
public class OrderUserController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // GET /api/orders/user/{userId}/active?page=1&size=10
    @GetMapping("/{userId}/active")
    public ResponseEntity<?> getActiveOrders(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng");
        }
        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getCustomerActiveOrders(user, pageIndex, size);
        List<OrderDTO> orderDTOs = orderPage.getContent().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
            java.util.Map.of(
                "orders", orderDTOs,
                "totalPages", orderPage.getTotalPages(),
                "totalElements", orderPage.getTotalElements(),
                "page", orderPage.getNumber() + 1
            )
        );
    }

    // GET /api/orders/user/{userId}/history?page=1&size=10
    @GetMapping("/{userId}/history")
    public ResponseEntity<?> getOrderHistory(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng");
        }
        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getCustomerHistory(user, pageIndex, size);
        List<OrderDTO> orderDTOs = orderPage.getContent().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
            java.util.Map.of(
                "orders", orderDTOs,
                "totalPages", orderPage.getTotalPages(),
                "totalElements", orderPage.getTotalElements(),
                "page", orderPage.getNumber() + 1
            )
        );
    }
}