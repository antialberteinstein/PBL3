package dut.gianguhohi.shoppiefood.controller.rest.shipper;

import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.dtos.OrderDTO;
import dut.gianguhohi.shoppiefood.services.OrderService;
import dut.gianguhohi.shoppiefood.services.ShipperService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
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
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/shipper")
public class OrderShipperController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private UserService userService;

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
            Map.of(
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
            Map.of(
                "orders", orderDTOs,
                "totalPages", orderPage.getTotalPages(),
                "totalElements", orderPage.getTotalElements(),
                "page", orderPage.getNumber() + 1
            )
        );
    }

    /**
     * API shipper nhận đơn hàng
     */
    @PostMapping("/take/{orderId}")
    public ResponseEntity<Map<String, Object>> takeOrder(
            @PathVariable int orderId,
            HttpSession session) {

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để nhận đơn hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền nhận đơn hàng");
        }

        try {
            Order order = orderService.getOrderById(orderId);

            if (!order.getStatus().equals(OrderStatusType.CONFIRMED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Chỉ có thể nhận đơn hàng khi trạng thái là 'đã xác nhận'");
            }

            if (order.getShipper() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng đã được shipper khác nhận");
            }

            orderService.assignShipperToOrder(orderId, shipper.getShipperId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Nhận đơn hàng thành công");

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
     * API shipper hủy nhận đơn hàng
     */
    @PostMapping("/untake/{orderId}")
    public ResponseEntity<Map<String, Object>> untakeOrder(
            @PathVariable int orderId,
            @RequestBody(required = false) Map<String, String> reason,
            HttpSession session) {

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để hủy nhận đơn hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền hủy nhận đơn hàng");
        }

        try {
            Order order = orderService.getOrderById(orderId);

            if (order.getShipper() == null || order.getShipper().getShipperId() != shipper.getShipperId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không phải là shipper của đơn hàng này");
            }

            if (!order.getStatus().equals(OrderStatusType.CONFIRMED) &&
                !order.getStatus().equals(OrderStatusType.PENDING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Chỉ có thể hủy nhận đơn hàng khi trạng thái là 'đang chờ xác nhận' hoặc 'đã xác nhận'");
            }

            if (order.getStatus().equals(OrderStatusType.IN_PROGRESS)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Không thể hủy nhận đơn hàng khi đang vận chuyển");
            }

            String reasonText = reason != null ? reason.get("reason") : "Shipper hủy nhận đơn";
            orderService.unassignShipperFromOrder(orderId, reasonText);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hủy nhận đơn hàng thành công");

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
     * API shipper xác nhận đã giao hàng
     */
    @PostMapping("/delivered/{orderId}")
    public ResponseEntity<Map<String, Object>> deliveredOrder(
            @PathVariable int orderId,
            HttpSession session) {

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để xác nhận giao hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xác nhận giao hàng");
        }

        try {
            Order order = orderService.getOrderById(orderId);

            if (order.getShipper() == null || order.getShipper().getShipperId() != shipper.getShipperId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không phải là shipper của đơn hàng này");
            }

            if (!order.getStatus().equals(OrderStatusType.IN_PROGRESS)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Chỉ có thể xác nhận giao hàng khi trạng thái là 'đang giao hàng'");
            }

            orderService.markOrderAsDelivered(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xác nhận giao hàng thành công");

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
     * API lấy danh sách đơn hàng sẵn sàng để shipper nhận
     */
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableOrders(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để xem đơn hàng");
        }

        int userId = (Integer) userIdObj;
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        Shipper shipper = shipperService.getShipperByUser(user);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem các đơn hàng này");
        }

        try {
            List<Order> availableOrders = orderService.getAvailableOrdersForShipper();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", availableOrders);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}