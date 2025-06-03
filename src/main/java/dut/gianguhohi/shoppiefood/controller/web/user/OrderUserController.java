package dut.gianguhohi.shoppiefood.controller.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.HashMap;

import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.utils.OrderStatusType;

@Controller
@RequestMapping("/api/user/order")
public class OrderUserController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * API đặt hàng từ người dùng
     */
    @PostMapping("/place")
    public ResponseEntity<Map<String, Object>> placeOrder(
            @RequestBody Map<String, Object> orderData,
            HttpSession session) {
        
        // Kiểm tra người dùng đã đăng nhập chưa
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để đặt hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        try {
            // Lấy dữ liệu từ request
            int restaurantId = (Integer) orderData.get("restaurantId");
            int addressId = (Integer) orderData.get("addressId");
            String note = (String) orderData.get("note");
            
            // Gọi service để xử lý đặt hàng
            Order createdOrder = orderService.createOrder(userId, restaurantId, addressId, orderData.get("items"), note);
            
            // Trả về kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đặt hàng thành công");
            response.put("orderId", createdOrder.getOrderId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * API hủy đơn hàng từ người dùng
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable int orderId,
            @RequestBody(required = false) Map<String, String> cancelReason,
            HttpSession session) {
        
        // Kiểm tra người dùng đã đăng nhập chưa
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để hủy đơn hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        try {
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderById(orderId);
            
            // Kiểm tra đơn hàng có thuộc về người dùng không
            if (order.getCustomer().getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền hủy đơn hàng này");
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!order.getStatus().equals(OrderStatusType.PENDING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Chỉ có thể hủy đơn hàng khi trạng thái là 'đang chờ xác nhận'");
            }
            
            // Thực hiện hủy đơn hàng
            String reason = cancelReason != null ? cancelReason.get("reason") : "Người dùng hủy";
            orderService.cancelOrder(orderId, reason, user);
            
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
}
