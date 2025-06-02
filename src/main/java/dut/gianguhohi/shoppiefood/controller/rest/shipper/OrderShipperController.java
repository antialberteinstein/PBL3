package dut.gianguhohi.shoppiefood.controller.rest.shipper;

import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/shipper")
public class OrderShipperController {

    @Autowired
    private OrderService orderService;

    // GET /api/orders/shipper/{shipperId}/active?page=1&size=10
    @GetMapping("/{shipperId}/active")
    public ResponseEntity<?> getActiveOrders(
            @PathVariable Shipper shipper,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getShipperActiveOrders(shipper, pageIndex, size);
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

    // GET /api/orders/shipper/{shipperId}/history?page=1&size=10
    @GetMapping("/{shipperId}/history")
    public ResponseEntity<?> getOrderHistory(
            @PathVariable Shipper shipper,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getShipperHistory(shipper, pageIndex, size);
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