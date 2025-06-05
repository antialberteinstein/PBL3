package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.OrderItem;
import dut.gianguhohi.shoppiefood.models.Product.Product;

import java.util.List;
import dut.gianguhohi.shoppiefood.models.Orders.Order;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    long countByProduct(Product product);
}
