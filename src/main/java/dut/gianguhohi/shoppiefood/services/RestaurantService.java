package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import java.util.List;
import java.util.Optional;
import dut.gianguhohi.shoppiefood.models.Users.User;
import java.time.LocalTime;

@Service
@Transactional
public class RestaurantService extends BaseService<Restaurant, Integer> {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    protected RestaurantRepository getRepository() {
        return restaurantRepository;
    }

    public List<Restaurant> getBySeller(User seller) {
        return restaurantRepository.findBySeller(seller);
    }

    public List<Restaurant> getOpenRestaurants() {
        LocalTime currentTime = LocalTime.now();
        return restaurantRepository.findByOpenTimeBeforeAndCloseTimeAfter(currentTime, currentTime);
    }

    public List<Restaurant> searchByName(String name) {
        return restaurantRepository.findByNameContains(name);
    }

    public Restaurant create(Restaurant restaurant) {
        validateEntity(restaurant);
        
        if (restaurantRepository.existsByName(restaurant.getName())) {
            throw new RestaurantRelatedException("Restaurant name already exists");
        }

        restaurant.setActive(true);
        return save(restaurant);
    }

    public Restaurant update(Integer id, Restaurant restaurant) {
        validateEntity(restaurant);
        Restaurant existingRestaurant = findById(id)
            .orElseThrow(() -> new RestaurantRelatedException("Restaurant not found"));

        // Check if new name conflicts with other restaurants
        if (!existingRestaurant.getName().equals(restaurant.getName()) && 
            restaurantRepository.existsByName(restaurant.getName())) {
            throw new RestaurantRelatedException("Restaurant name already exists");
        }

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setDescription(restaurant.getDescription());
        existingRestaurant.setAddress(restaurant.getAddress());
        existingRestaurant.setPhoneNumber(restaurant.getPhoneNumber());
        existingRestaurant.setOpenTime(restaurant.getOpenTime());
        existingRestaurant.setCloseTime(restaurant.getCloseTime());
        existingRestaurant.setImageUrl(restaurant.getImageUrl());
        
        return save(existingRestaurant);
    }

    public Restaurant toggleStatus(Integer id) {
        Restaurant restaurant = findById(id)
            .orElseThrow(() -> new RestaurantRelatedException("Restaurant not found"));
        
        restaurant.setActive(!restaurant.isActive());
        return save(restaurant);
    }

    public Restaurant updateOperatingHours(Integer id, LocalTime openTime, LocalTime closeTime) {
        Restaurant restaurant = findById(id)
            .orElseThrow(() -> new RestaurantRelatedException("Restaurant not found"));
        
        if (openTime.isAfter(closeTime)) {
            throw new RestaurantRelatedException("Open time must be before close time");
        }
        
        restaurant.setOpenTime(openTime);
        restaurant.setCloseTime(closeTime);
        return save(restaurant);
    }

    public void delete(Integer id) {
        Restaurant restaurant = findById(id)
            .orElseThrow(() -> new RestaurantRelatedException("Restaurant not found"));
        delete(restaurant);
    }

    public static class RestaurantRelatedException extends RuntimeException {
        public RestaurantRelatedException(String message) {
            super(message);
        }
    }
}
