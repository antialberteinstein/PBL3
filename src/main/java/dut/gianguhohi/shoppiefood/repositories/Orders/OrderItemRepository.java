package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.OrderItem;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Orders.Order;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
}
