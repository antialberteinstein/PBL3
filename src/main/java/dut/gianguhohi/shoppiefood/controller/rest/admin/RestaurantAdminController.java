package dut.gianguhohi.shoppiefood.controller.rest.admin;

import dut.gianguhohi.shoppiefood.dtos.RestaurantDTO;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/restaurants")
public class RestaurantAdminController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);

        List<RestaurantDTO> dtos = restaurantPage.getContent().stream()
                .map(RestaurantDTO::new)
                .toList();

        Map<String, Object> response = Map.of(
                "restaurants", dtos,
                "currentPage", restaurantPage.getNumber(),
                "totalItems", restaurantPage.getTotalElements(),
                "totalPages", restaurantPage.getTotalPages()
        );
        return ResponseEntity.ok(response);
    }
}