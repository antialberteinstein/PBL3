package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.services.RestaurantService;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurant")
public class BRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/enter")
    public ResponseEntity<?> enter(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            session.setAttribute("role", "restaurant");
            return ResponseEntity.ok(Map.of("message", "Entered restaurant mode"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<?> exit(HttpSession session) {
        session.removeAttribute("role");
        return ResponseEntity.ok(Map.of("message", "Exited restaurant mode"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody Restaurant restaurant,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            restaurant.setSeller(user);
            Restaurant savedRestaurant = restaurantService.create(restaurant);
            return ResponseEntity.ok(savedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
        @PathVariable Integer id,
        @RequestBody Restaurant restaurant,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Restaurant updatedRestaurant = restaurantService.update(id, restaurant);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleStatus(
        @PathVariable Integer id,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Restaurant restaurant = restaurantService.toggleStatus(id);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-restaurants")
    public ResponseEntity<?> getMyRestaurants(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            return ResponseEntity.ok(restaurantService.getBySeller(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}