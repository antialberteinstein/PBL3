package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart_CartId(Integer cartId);
}
