package dut.gianguhohi.shoppiefood.services.Products;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(int id) {
        return orderRepository.findByOrderId(id);
    }

    public Order placeOrder(Order order) {
        return orderRepository.save(order);
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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public static class OrderRelatedException extends RuntimeException {
        public OrderRelatedException(String message) {
            super(message);
        }
    } 
}
