package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.utils.Validator;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;

@Transactional
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(int id) {
        validateId(id);
        Order order = orderRepository.findByOrderId(id);
        if (order == null) {
            throw new AppServiceException("Không tìm thấy đơn hàng với ID: " + id);
        }
        return order;
    }

    public Order placeOrder(Order order) {
        validateOrder(order);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(User user) {
        validateUser(user);
        return orderRepository.findByCustomer(user);
    }

    public List<Order> getOrdersByRestaurant(Restaurant restaurant) {
        validateRestaurant(restaurant);
        return orderRepository.findByRestaurant(restaurant);
    }

    public List<Order> getOrdersByShipper(Shipper shipper) {
        validateShipper(shipper);
        return orderRepository.findByShipper(shipper);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(int id) {
        validateId(id);
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    /* Validation phase */
    private void validateId(int id) {
        if (id <= 0) {
            throw new AppServiceException("ID đơn hàng không hợp lệ");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new AppServiceException("Khách hàng không hợp lệ");
        }
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new AppServiceException("Nhà hàng không hợp lệ");
        }
    }

    private void validateShipper(Shipper shipper) {
        if (shipper == null) {
            throw new AppServiceException("Shipper không hợp lệ");
        }
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new AppServiceException("Đơn hàng không hợp lệ");
        }
        // Add more field validations as needed
    }
}