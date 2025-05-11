package dut.gianguhohi.shoppiefood.repositories.Users;

import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    
    List<Restaurant> findBySeller(User seller);

    Restaurant findByRestaurantId(int restaurantId);
}
