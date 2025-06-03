package dut.gianguhohi.shoppiefood.controller.rest.admin;

import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
public class OrderAdminController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDTO> orderDTOs = orderPage.getContent().stream()
                .map(OrderDTO::new)
                .toList();

        Map<String, Object> response = Map.of(
                "orders", orderDTOs,
                "currentPage", orderPage.getNumber(),
                "totalItems", orderPage.getTotalElements(),
                "totalPages", orderPage.getTotalPages()
        );
        return ResponseEntity.ok(response);
    }
}