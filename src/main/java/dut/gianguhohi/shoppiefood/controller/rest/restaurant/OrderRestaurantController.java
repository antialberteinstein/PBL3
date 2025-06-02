package dut.gianguhohi.shoppiefood.controller.rest.restaurant;

import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import dut.gianguhohi.shoppiefood.services.restaurant.BranchService;

@RestController
@RequestMapping("/api/orders/restaurant")
public class OrderRestaurantController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BranchService branchService;

    // GET /api/orders/restaurant/branch/{branchId}/active?page=1&size=10
    @GetMapping("/branch/{branchId}/active")
    public ResponseEntity<?> getBranchActiveOrders(
            @PathVariable int branchId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Branch branch = branchService.readBranchById(branchId);

        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getBranchActiveOrders(branch, pageIndex, size);
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

    // GET /api/orders/restaurant/branch/{branchId}/history?page=1&size=10
    @GetMapping("/branch/{branchId}/history")
    public ResponseEntity<?> getBranchOrderHistory(
            @PathVariable int branchId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Branch branch = branchService.readBranchById(branchId);

        int pageIndex = Math.max(page - 1, 0);
        Page<Order> orderPage = orderService.getBranchHistory(branch, pageIndex, size);
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