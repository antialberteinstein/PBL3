package dut.gianguhohi.shoppiefood.services.Restaurants;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getBySeller(User seller) {
        return restaurantRepository.findBySeller(seller);
    }

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant readById(int id) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        return restaurant;
    }

    public static class RestaurantRelatedException extends RuntimeException {
        public RestaurantRelatedException(String message) {
            super(message);
        }
    }

}
