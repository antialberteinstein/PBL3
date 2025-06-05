package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import dut.gianguhohi.shoppiefood.repositories.Users.ShipperRepository;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.repositories.misc.BranchRepository;
import dut.gianguhohi.shoppiefood.utils.OrderStatusType;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Branch;

@Transactional
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private ShipperRepository shipperRepository;

    // ==========================
    // == Order Query Methods  ==
    // ==========================
    public Order getOrderById(int id) {
        Order order = orderRepository.findByOrderId(id);
        if (order == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Không tìm thấy đơn hàng với ID: " + id
            );
        }
        return order;
    }

    public Page<Order> getCustomerHistory(User user, int page, int size) {
        validateUser(user);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getCustomerHistory(user, pageable);
    }

    public Page<Order> getCustomerActiveOrders(User user, int page, int size) {
        validateUser(user);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getCustomerActiveOrders(user, pageable);
    }

    public Page<Order> getBranchHistory(Branch branch, int page, int size) {
        validateBranch(branch);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getBranchHistory(branch, pageable);
    }

    public Page<Order> getBranchActiveOrders(Branch branch, int page, int size) {
        validateBranch(branch);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getBranchActiveOrders(branch, pageable);
    }

    public Page<Order> getShipperHistory(Shipper shipper, int page, int size) {
        validateShipper(shipper);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getShipperHistory(shipper, pageable);
    }

    public Page<Order> getShipperActiveOrders(Shipper shipper, int page, int size) {
        validateShipper(shipper);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getShipperActiveOrders(shipper, pageable);
    }

    // ==========================
    // == Order CRUD Methods   ==
    // ==========================
    public Order placeOrder(Order order) {
        validateOrder(order);
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        validateId(id);
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    // ==========================
    // == Validation Methods   ==
    // ==========================
    private void validateId(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID đơn hàng không hợp lệ");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khách hàng không hợp lệ");
        }
    }

    private void validateShipper(Shipper shipper) {
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shipper không hợp lệ");
        }
    }

    private void validateBranch(Branch branch) {
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi nhánh không hợp lệ");
        }
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng không hợp lệ");
        }
        // Add more field validations as needed
    }
    
    /**
     * Tạo đơn hàng mới
     */
    public Order createOrder(int userId, int branchId, int addressId, Object items, String note) {
        // Logic tạo đơn hàng sẽ được triển khai tại đây
        // Bao gồm kiểm tra user, restaurant, address tồn tại
        // Kiểm tra các món hàng hợp lệ
        // Tính toán tổng tiền, v.v.
        
        User user = userRepository.findByUserId(userId);
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng");
        }

        Branch branch = branchRepository.findByBranchId(branchId);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh");
        }

        // Tạo đơn hàng với trạng thái pending
        Order order = new Order();
        order.setCustomer(user);
        order.setBranch(branch);
        // Set các thông tin khác của đơn hàng
        order.setStatus(OrderStatusType.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setNote(note);
        
        // Lưu đơn hàng
        return orderRepository.save(order); 
    }
    
    /**
     * Xác nhận đơn hàng
     */
    @Transactional
    public Order confirmOrder(int orderId, Integer estimateMinutes) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (!order.getStatus().equals(OrderStatusType.PENDING)) {
            throw new RuntimeException("Chỉ có thể xác nhận đơn hàng khi trạng thái là 'đang chờ xác nhận'");
        }
        
        order.setStatus(OrderStatusType.CONFIRMED);
        order.setTimeConfirmed(LocalDateTime.now());        
            
        // Nếu có thông tin thời gian ước tính
        if (estimateMinutes != null) {
            // Logic để tính thời gian giao hàng ước tính
            LocalDateTime estimatedDelivery = LocalDateTime.now().plusMinutes(estimateMinutes);
            order.setEstimatedDeliveryTime(estimatedDelivery);
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * Gán shipper cho đơn hàng
     */
    /**
     * Gán shipper cho đơn hàng (chỉ gán shipper, không đổi trạng thái)
     */
    public Order assignShipperToOrder(int orderId, int shipperId) {
        Order order = getOrderById(orderId);

        if (!order.getStatus().equals(OrderStatusType.CONFIRMED)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chỉ có thể gán shipper khi đơn hàng đã được xác nhận"
            );
        }

        if (order.getShipper() != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đơn hàng đã được shipper khác nhận"
            );
        }

        Shipper shipper = shipperRepository.findByShipperId(shipperId);
        if (shipper == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Không tìm thấy shipper với ID: " + shipperId
            );
        }

        order.setShipper(shipper);
        // Không đổi trạng thái ở đây
        return orderRepository.save(order);
    }

    /**
     * Shipper bắt đầu giao hàng (đổi trạng thái sang IN_PROGRESS)
     */
    public Order shipperStartDelivery(int orderId, int shipperId) {
        Order order = getOrderById(orderId);

        if (!order.getStatus().equals(OrderStatusType.CONFIRMED)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chỉ có thể bắt đầu giao hàng khi đơn hàng đã được xác nhận"
            );
        }

        if (order.getShipper() == null || order.getShipper().getShipperId() != shipperId) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Shipper không hợp lệ hoặc chưa được gán cho đơn hàng này"
            );
        }

        order.setStatus(OrderStatusType.IN_PROGRESS);
        order.setTimeStart(LocalDateTime.now());

        return orderRepository.save(order);
    }
    
    /**
     * Hủy gán shipper cho đơn hàng
     */
    public Order unassignShipperFromOrder(int orderId, String reason) {
        Order order = getOrderById(orderId);
        
        if (order.getShipper() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đơn hàng này chưa có shipper nào nhận"
            );
        }
        
        if (order.getStatus().equals(OrderStatusType.IN_PROGRESS)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Không thể hủy gán shipper khi đơn hàng đang trong quá trình giao hàng"
            );
        }
        
        // Ghi log lý do hủy gán
        order.setCancelReason("Shipper hủy nhận đơn: " + reason);
        order.setShipper(null);
        
        // Trả về trạng thái đã xác nhận để shipper khác có thể nhận
        order.setStatus(OrderStatusType.CONFIRMED);
        
        return orderRepository.save(order);
    }
    
    /**
     * Xác nhận đã giao hàng
     */
    public Order markOrderAsDelivered(int orderId) {
        Order order = getOrderById(orderId);
        
        if (!order.getStatus().equals(OrderStatusType.IN_PROGRESS)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chỉ có thể đánh dấu đơn hàng là đã giao khi trạng thái là 'đang giao hàng'"
            );
        }
        
        order.setStatus(OrderStatusType.DELIVERED);
        order.setTimeDelivered(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Hủy đơn hàng bởi người dùng
     */
    public Order cancelOrder(int orderId, String reason, User user) {
        Order order = getOrderById(orderId);
        
        if (order.getCustomer().getUserId() != user.getUserId()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Đơn hàng này không thuộc về của bạn"
            );
        }
        
        if (!order.getStatus().equals(OrderStatusType.PENDING)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chỉ có thể hủy đơn hàng khi trạng thái là 'đang chờ xác nhận'"
            );
        }
        
        order.setStatus(OrderStatusType.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelledDate(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Hủy đơn hàng bởi nhà hàng
     */
    public Order cancelOrderByRestaurant(int orderId, String reason, Restaurant restaurant) {
        Order order = getOrderById(orderId);
        
        // Kiểm tra chi nhánh có thuộc restaurant không
        if (order.getBranch().getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Đơn hàng này không thuộc về nhà hàng của bạn"
            );
        }
        
        if (!order.getStatus().equals(OrderStatusType.PENDING) && 
            !order.getStatus().equals(OrderStatusType.CONFIRMED)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chỉ có thể hủy đơn hàng khi trạng thái là 'đang chờ xác nhận' hoặc 'đã xác nhận'"
            );
        }
        
        order.setStatus(OrderStatusType.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelledDate(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Lấy danh sách đơn hàng theo trạng thái của một nhà hàng
     */
    public List<Order> getOrdersByRestaurantAndStatus(int restaurantId, String status) {
        return orderRepository.findByBranch_Restaurant_RestaurantIdAndStatusOrderByCreatedAtDesc(restaurantId, status);
    }
    
    /**
     * Lấy danh sách đơn hàng sẵn sàng cho shipper nhận
     */
    public List<Order> getAvailableOrdersForShipper() {
        return orderRepository.findByStatusAndShipperIsNullOrderByCreatedAtAsc(OrderStatusType.CONFIRMED);
    }
    
    /**
     * Lấy danh sách đơn hàng đang được giao bởi một shipper
     */
    public List<Order> getCurrentOrdersByShipper(Shipper shipper) {
        return orderRepository.findByShipperAndStatusOrderByCreatedAtDesc(shipper, OrderStatusType.IN_PROGRESS);
    }
    
    /**
     * Lấy danh sách đơn hàng đã giao của một shipper
     */
    public List<Order> getDeliveredOrdersByShipper(Shipper shipper) {
        return orderRepository.findByShipperAndStatusOrderByTimeDeliveredDesc(shipper, OrderStatusType.DELIVERED);
    }
}