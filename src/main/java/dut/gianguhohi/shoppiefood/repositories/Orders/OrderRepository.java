package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser_UserId(Integer userId);

    List<Order> findByRestaurant_RestaurantId(Integer restaurantId);

    List<Order> findByShipper_ShipperId(Integer shipperId);
}
