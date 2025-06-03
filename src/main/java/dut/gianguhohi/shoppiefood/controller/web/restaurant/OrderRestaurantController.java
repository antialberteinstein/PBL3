package dut.gianguhohi.shoppiefood.controller.web.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;
import dut.gianguhohi.shoppiefood.utils.OrderStatusType;

@Controller
@RequestMapping("/api/restaurant/order")
public class OrderRestaurantController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * API xác nhận đơn hàng từ nhà hàng
     */
    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<Map<String, Object>> confirmOrder(
            @PathVariable int orderId,
            @RequestBody(required = false) Map<String, Object> estimateData,
            HttpSession session) {
        
        // Kiểm tra nhà hàng đã đăng nhập chưa
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
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderById(orderId);
            // Kiểm tra đơn hàng có thuộc về nhà hàng không
            if (order.getBranch().getRestaurant().getRestaurantId() != restaurantId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Đơn hàng này không thuộc về nhà hàng của bạn");
            }

            // Kiểm tra trạng thái đơn hàng
            if (!order.getStatus().equals(OrderStatusType.PENDING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Chỉ có thể xác nhận đơn hàng khi trạng thái là 'đang chờ xác nhận'");
            }
            
            // Thời gian ước tính (nếu có)
            Integer estimateMinutes = null;
            if (estimateData != null && estimateData.containsKey("estimateMinutes")) {
                estimateMinutes = (Integer) estimateData.get("estimateMinutes");
            }
            
            // Thực hiện xác nhận đơn hàng
            orderService.confirmOrder(orderId, estimateMinutes);
            
            // Trả về kết quả
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
        
        // Kiểm tra nhà hàng đã đăng nhập chưa
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
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderById(orderId);
            // Kiểm tra đơn hàng có thuộc về nhà hàng không
            if (order.getBranch().getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Đơn hàng này không thuộc về nhà hàng của bạn");
            }

            // Kiểm tra trạng thái đơn hàng
            if (!order.getStatus().equals(OrderStatusType.PENDING) &&
                !order.getStatus().equals(OrderStatusType.CONFIRMED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Chỉ có thể hủy đơn hàng khi trạng thái là 'đang chờ xác nhận' hoặc 'đã xác nhận'");
            }

            // Thực hiện hủy đơn hàng
            String reason = cancelReason != null ? cancelReason.get("reason") : "Nhà hàng hủy";
            orderService.cancelOrderByRestaurant(orderId, reason, restaurant);
            
            // Trả về kết quả
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
        // Kiểm tra nhà hàng đã đăng nhập chưa
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
}