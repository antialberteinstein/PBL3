package dut.gianguhohi.shoppiefood.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.UserService;
import org.springframework.data.domain.Page;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // API: GET /api/orders/customer/{customerId}?page=1&size=10
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomer(
            @PathVariable int customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = userService.readById(customerId);
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
}