package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser_UserId(Integer userId);
}
