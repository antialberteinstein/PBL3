package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.services.ShipperService;
import java.util.Map;

@RestController
@RequestMapping("/api/shipper")
public class BShipperController {

    @Autowired
    private ShipperService shipperService;

    @PostMapping("/enter")
    public ResponseEntity<?> enter(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            session.setAttribute("role", "shipper");
            return ResponseEntity.ok(Map.of("message", "Entered shipper mode"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<?> exit(HttpSession session) {
        session.removeAttribute("role");
        return ResponseEntity.ok(Map.of("message", "Exited shipper mode"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestParam("vehicleType") String vehicleType,
        @RequestParam("vehicleNumber") String vehicleNumber,
        @RequestParam("driverLicense") String driverLicense,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Shipper shipper = shipperService.register(user, vehicleType, vehicleNumber, driverLicense);
            return ResponseEntity.ok(shipper);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
        @PathVariable Integer id,
        @RequestBody Shipper shipper,
        HttpSession session
    ) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not logged in"));
            }
            Shipper updatedShipper = shipperService.update(id, shipper);
            return ResponseEntity.ok(updatedShipper);
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
            Shipper shipper = shipperService.toggleStatus(id);
            return ResponseEntity.ok(shipper);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
}