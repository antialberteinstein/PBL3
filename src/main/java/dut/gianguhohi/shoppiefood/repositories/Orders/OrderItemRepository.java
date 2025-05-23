package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.OrderItem;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_OrderId(Integer orderId);
}
