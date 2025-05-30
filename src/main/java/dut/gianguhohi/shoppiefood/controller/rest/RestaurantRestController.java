package dut.gianguhohi.shoppiefood.controller.rest;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import dut.gianguhohi.shoppiefood.services.RestaurantService;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.dtos.BranchDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

@RestController
public class RestaurantRestController {

    // ==========================
    // == Service Injection    ==
    // ==========================
    @Autowired
    private RestaurantService restaurantService;



    // ==========================
    // == Branch APIs         ==
    // ==========================

    // Create branch
    @PostMapping("/api/restaurant/{restaurantId}/addBranch")
    public ResponseEntity<?> addBranch(
        @PathVariable int restaurantId,
        @RequestBody BranchRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = restaurantService.createBranch(
            restaurant,
            req.getBranchName(),
            req.getPhoneNumber(),
            req.getStartTime(),
            req.getEndTime(),
            req.getCity(),
            req.getWard(),
            req.getAddressLine1(),
            req.getAddressLine2()
        );
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    // Get branches by restaurant
    @GetMapping("/api/restaurant/{id}/branches")
    public ResponseEntity<?> getBranchesByRestaurant(
        @PathVariable int id,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size
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

    // Get branch by id
    @GetMapping("/api/restaurant/branch/{branchId}")
    public ResponseEntity<?> getBranchById(@PathVariable int branchId) {
        Branch branch = restaurantService.readBranchById(branchId);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    // Update branch
    @PutMapping("/api/restaurant/{restaurantId}/branch/{branchId}")
    public ResponseEntity<?> updateBranch(
        @PathVariable int restaurantId,
        @PathVariable int branchId,
        @RequestBody BranchRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = restaurantService.updateBranch(
            restaurant,
            branchId,
            req.getBranchName(),
            req.getPhoneNumber(),
            req.getStartTime(),
            req.getEndTime(),
            req.getCity(),
            req.getWard(),
            req.getAddressLine1(),
            req.getAddressLine2()
        );
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    // Delete branch
    @DeleteMapping("/api/restaurant/branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable int branchId) {
        restaurantService.deleteBranch(branchId);
        return ResponseEntity.ok().build(Map.of("success", true));
    }




    // ==========================
    // == Restaurant APIs      ==
    // ==========================

    // Update restaurant
    @PutMapping("/api/restaurant/{id}")
    public ResponseEntity<?> updateRestaurant(
        @PathVariable int id,
        @RequestBody RestaurantRequest req
    ) {

        restaurantService.update(
            id,
            req.getName(),
            req.getDescription(),
            req.getBackgroundUrl()
        );

        return ResponseEntity.ok(Map.of("success", true));
    }





    // ==========================
    // == Request Classes      ==
    // ==========================

    /**
     * Request class for creating or updating a branch.
     */
    public static class BranchRequest {
        private String branchName;
        private String phoneNumber;
        private String startTime;
        private String endTime;
        private String city;
        private String ward;
        private String addressLine1;
        private String addressLine2;

        // Getters and Setters
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getWard() { return ward; }
        public void setWard(String ward) { this.ward = ward; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getAddressLine2() { return addressLine2; }
        public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    }

    /**
     * Request class for updating a restaurant.
     */
    public static class RestaurantRequest {
        private String name;
        private String description;
        private String backgroundUrl;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getBackgroundUrl() { return backgroundUrl; }
        public void setBackgroundUrl(String backgroundUrl) { this.backgroundUrl = backgroundUrl; }
    }
}