package dut.gianguhohi.shoppiefood.controller.rest.restaurant;

import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.restaurant.BranchService;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;
import dut.gianguhohi.shoppiefood.utils.OrderStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/restaurant")
public class OrderRestaurantController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RestaurantService restaurantService;

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
            Map.of(
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
            Map.of(
                "orders", orderDTOs,
                "totalPages", orderPage.getTotalPages(),
                "totalElements", orderPage.getTotalElements(),
                "page", orderPage.getNumber() + 1
            )
        );
    }

    /**
     * API xác nhận đơn hàng từ nhà hàng
     */
    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<Map<String, Object>> confirmOrder(
            @PathVariable int orderId,
            @RequestBody(required = false) Map<String, Object> estimateData,
            HttpSession session) {

        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền xác nhận đơn hàng");
        }

        int restaurantId = (Integer) restaurantIdObj;
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }

        try {
            Order order = orderService.getOrderById(orderId);
            if (order.getBranch().getRestaurant().getRestaurantId() != restaurantId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Đơn hàng này không thuộc về nhà hàng của bạn");
            }
            if (!order.getStatus().equals(OrderStatusType.PENDING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Chỉ có thể xác nhận đơn hàng khi trạng thái là 'đang chờ xác nhận'");
            }

            Integer estimateMinutes = null;
            if (estimateData != null && estimateData.containsKey("estimateMinutes")) {
                estimateMinutes = (Integer) estimateData.get("estimateMinutes");
            }

            orderService.confirmOrder(orderId, estimateMinutes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xác nhận đơn hàng thành công");

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * API hủy đơn hàng từ nhà hàng
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable int orderId,
            @RequestBody(required = false) Map<String, String> cancelReason,
            HttpSession session) {

        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền hủy đơn hàng");
        }

        int restaurantId = (Integer) restaurantIdObj;
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }

        try {
            Order order = orderService.getOrderById(orderId);
            if (order.getBranch().getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Đơn hàng này không thuộc về nhà hàng của bạn");
            }
            if (!order.getStatus().equals(OrderStatusType.PENDING) &&
                !order.getStatus().equals(OrderStatusType.CONFIRMED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Chỉ có thể hủy đơn hàng khi trạng thái là 'đang chờ xác nhận' hoặc 'đã xác nhận'");
            }

            String reason = cancelReason != null ? cancelReason.get("reason") : "Nhà hàng hủy";
            orderService.cancelOrderByRestaurant(orderId, reason, restaurant);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hủy đơn hàng thành công");

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * API lấy danh sách đơn hàng đang chờ xác nhận của nhà hàng
     */
    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingOrders(HttpSession session) {
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }

        int restaurantId = (Integer) restaurantIdObj;

        try {
            List<Order> pendingOrders = orderService.getOrdersByRestaurantAndStatus(restaurantId, OrderStatusType.PENDING);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", pendingOrders);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * API lấy chi tiết đơn hàng
     */
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(
            @PathVariable int orderId,
            HttpSession session) {
    
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }

        int restaurantId = (Integer) restaurantIdObj;
    
        try {
            Order order = orderService.getOrderById(orderId);
        
            // Kiểm tra order có thuộc về restaurant không
            if (!Objects.equals(order.getBranch().getRestaurant().getRestaurantId(), restaurantId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Đơn hàng này không thuộc về nhà hàng của bạn");
            }
        
            // Trả về thông tin chi tiết đơn hàng
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", new OrderDTO(order));
        
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
        
            return ResponseEntity.badRequest().body(response);
        }
    }
}