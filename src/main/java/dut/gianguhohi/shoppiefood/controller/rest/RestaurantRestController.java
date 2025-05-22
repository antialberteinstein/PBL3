package dut.gianguhohi.shoppiefood.controller.rest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import dut.gianguhohi.shoppiefood.services.RestaurantService;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.dtos.BranchDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

import org.springframework.data.domain.Page;
import java.util.Map;


@RestController
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/api/restaurant/addBranch")
    public ResponseEntity<?> addBranch(
        @RequestParam String branchName,
        @RequestParam String phoneNumber,
        @RequestParam String startTime,
        @RequestParam String endTime,
        @RequestParam String city,
        @RequestParam String ward,
        @RequestParam String addressLine1,
        @RequestParam String addressLine2,
        HttpSession session
    ) {
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }

        Branch branch = restaurantService.createBranch(restaurant, branchName, phoneNumber, startTime, endTime, city, ward, addressLine1, addressLine2);

        BranchDTO branchDTO = new BranchDTO(branch);

        return ResponseEntity.ok(branchDTO);
    }

    @GetMapping("/api/restaurant/{id}/branches")
    public ResponseEntity<?> getBranchesByRestaurant(
        @PathVariable int id,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size,
        HttpSession session
    ) {
        Restaurant restaurant = restaurantService.readById(id);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }

        int exactPage = (page > 0) ? page - 1 : 0;

        Page<Branch> branches = restaurantService.getByRestaurant(restaurant, exactPage, size);
        List<BranchDTO> branchDTOs = branches.stream().map(BranchDTO::new).toList();
        return ResponseEntity.ok(
            Map.of(
                "branches", branchDTOs,
                "totalPages", branches.getTotalPages(),
                "totalElements", branches.getTotalElements(),
                "page", branches.getNumber() + 1
            )
        );
    }

    @GetMapping("/api/restaurant/branch/{branchId}")
    public ResponseEntity<?> getBranchById(@PathVariable int branchId, HttpSession session) {
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = restaurantService.readBranchById(branchId);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    @PutMapping("/api/restaurant/branch/{branchId}")
    public ResponseEntity<?> updateBranch(
        @PathVariable int branchId,
        @RequestParam String branchName,
        @RequestParam String phoneNumber,
        @RequestParam String startTime,
        @RequestParam String endTime,
        @RequestParam String city,
        @RequestParam String ward,
        @RequestParam String addressLine1,
        @RequestParam String addressLine2,
        HttpSession session
    ) {
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = restaurantService.updateBranch(restaurant, branchId, branchName, phoneNumber, startTime, endTime, city, ward, addressLine1, addressLine2);
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    @DeleteMapping("/api/restaurant/branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable int branchId, HttpSession session) {
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        restaurantService.deleteBranch(restaurant, branchId);
        return ResponseEntity.ok().build();
    }
}