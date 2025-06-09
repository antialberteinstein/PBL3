package dut.gianguhohi.shoppiefood.services.restaurant;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.User;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // Get restaurant by seller
    public List<Restaurant> getBySeller(User seller) {
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người bán không hợp lệ");
        }
        return restaurantRepository.findBySeller(seller);
    }

    // Get restaurant by id
    public Restaurant readById(int id) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng");
        }
        return restaurant;
    }

    // Create new restaurant
    public Restaurant create(User seller, String name, String description) {
        validateRestaurant(name, description, seller);
        Restaurant restaurant = new Restaurant(name, description, seller);
        return restaurantRepository.save(restaurant);
    }

    // Update restaurant
    public Restaurant update(int id, String name, String description, String backgroundUrl) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID nhà hàng không hợp lệ");
        }
        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng");
        }
        validateRestaurant(name, description, restaurant.getSeller());
        restaurant.setRestaurantName(name);
        restaurant.setDescription(description);
        if (backgroundUrl != null && !backgroundUrl.trim().isEmpty()) {
            restaurant.setBackgroundUrl(backgroundUrl);
        }
        return restaurantRepository.save(restaurant);
    }

    // Validation
    private void validateRestaurant(String name, String description, User seller) {
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người bán không hợp lệ");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên nhà hàng không được để trống");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên nhà hàng phải từ 2 đến 100 ký tự");
        }
        if (description != null && description.length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mô tả nhà hàng không được vượt quá 500 ký tự");
        }
    }

    public Restaurant getRestaurantById(int restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
    }
}