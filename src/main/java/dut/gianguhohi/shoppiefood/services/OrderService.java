package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import dut.gianguhohi.shoppiefood.models.misc.Branch;

@Transactional
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


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
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi nhánh không hợp lệ");
        }
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getBranchHistory(branch, pageable);
    }

    public Page<Order> getBranchActiveOrders(Branch branch, int page, int size) {
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi nhánh không hợp lệ");
        }
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

    public Order placeOrder(Order order) {
        validateOrder(order);
        return orderRepository.save(order);
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