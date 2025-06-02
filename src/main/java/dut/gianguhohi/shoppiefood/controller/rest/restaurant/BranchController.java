package dut.gianguhohi.shoppiefood.controller.rest.restaurant;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.services.restaurant.*;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.dtos.BranchDTO;
import dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos.BranchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurant")
public class BranchController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private BranchService branchService;

    // ==========================
    // == Branch APIs         ==
    // ==========================

    @PostMapping("/{restaurantId}/branch")
    public ResponseEntity<?> addBranch(
            @PathVariable int restaurantId,
            @RequestBody BranchRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = branchService.createBranch(
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

    @GetMapping("/{id}/branch")
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
        Page<Branch> branches = branchService.getByRestaurant(restaurant, exactPage, size);
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

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<?> getBranchById(@PathVariable int branchId) {
        Branch branch = branchService.readBranchById(branchId);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }
        return ResponseEntity.ok(new BranchDTO(branch));
    }

    @PutMapping("/{restaurantId}/branch/{branchId}")
    public ResponseEntity<?> updateBranch(
            @PathVariable int restaurantId,
            @PathVariable int branchId,
            @RequestBody BranchRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Branch branch = branchService.updateBranch(
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

    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable int branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}