package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Orders.OrderStatus;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Service
@Transactional
public class OrderService extends BaseService<Order, Integer> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected OrderRepository getRepository() {
        return orderRepository;
    }

    public Order createOrder(Order order) {
        validateEntity(order);
        order.setStatus(OrderStatus.PENDING);
        return save(order);
    }

    public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = findById(orderId)
            .orElseThrow(() -> new OrderRelatedException("Order not found"));
        
        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return save(order);
    }

    public Order assignShipper(Integer orderId, Shipper shipper) {
        Order order = findById(orderId)
            .orElseThrow(() -> new OrderRelatedException("Order not found"));
        
        if (order.getStatus() != OrderStatus.READY_FOR_DELIVERY) {
            throw new OrderRelatedException("Order is not ready for delivery");
        }
        
        order.setShipper(shipper);
        order.setStatus(OrderStatus.ASSIGNED_TO_SHIPPER);
        return save(order);
    }

    public Order cancelOrder(Integer orderId, User user) {
        Order order = findById(orderId)
            .orElseThrow(() -> new OrderRelatedException("Order not found"));
        
        if (!order.getCustomer().equals(user)) {
            throw new OrderRelatedException("User is not authorized to cancel this order");
        }
        
        if (!canBeCancelled(order.getStatus())) {
            throw new OrderRelatedException("Order cannot be cancelled in its current status");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        return save(order);
    }

    public List<Order> getOrdersByCustomer(User user) {
        return orderRepository.findByCustomer(user);
    }

    public List<Order> getOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.findByRestaurant(restaurant);
    }

    public List<Order> getOrdersByShipper(Shipper shipper) {
        return orderRepository.findByShipper(shipper);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new OrderRelatedException(
                String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                return next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED:
                return next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING:
                return next == OrderStatus.READY_FOR_DELIVERY || next == OrderStatus.CANCELLED;
            case READY_FOR_DELIVERY:
                return next == OrderStatus.ASSIGNED_TO_SHIPPER || next == OrderStatus.CANCELLED;
            case ASSIGNED_TO_SHIPPER:
                return next == OrderStatus.DELIVERING || next == OrderStatus.CANCELLED;
            case DELIVERING:
                return next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }

    private boolean canBeCancelled(OrderStatus status) {
        return status == OrderStatus.PENDING || 
               status == OrderStatus.CONFIRMED || 
               status == OrderStatus.PREPARING;
    }

    public static class OrderRelatedException extends RuntimeException {
        public OrderRelatedException(String message) {
            super(message);
        }
    }
}
