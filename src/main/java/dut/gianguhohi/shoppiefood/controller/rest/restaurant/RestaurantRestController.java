package dut.gianguhohi.shoppiefood.controller.rest.restaurant;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;
import dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos.RestaurantRequest;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private HttpSession session;

    // ==========================
    // == Restaurant APIs      ==
    // ==========================

    @PutMapping
    public ResponseEntity<?> updateRestaurant(
            @RequestBody RestaurantRequest req
    ) {
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Bạn không có quyền hạn truy cập"));
        }
        Integer id = (Integer) restaurantIdObj;
        restaurantService.update(
                id,
                req.getName(),
                req.getDescription(),
                req.getBackgroundUrl()
        );
        return ResponseEntity.ok(Map.of("success", true));
    }

    // Add more restaurant-level APIs here if needed
}