package dut.gianguhohi.shoppiefood.controller.rest.admin;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import dut.gianguhohi.shoppiefood.dtos.UserDTO;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repo;

    // List all users with pagination
    @GetMapping
    public ResponseEntity<Map<String, Object>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = repo.findAll(pageable);

        // Map User to UserDTO
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(UserDTO::new)
                .toList();

        Map<String, Object> response = Map.of(
                "users", userDTOs,
                "currentPage", userPage.getNumber(),
                "totalItems", userPage.getTotalElements(),
                "totalPages", userPage.getTotalPages()
        );
        return ResponseEntity.ok(response);
    }

    // Disable a user
    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable int id) {
        userService.disable(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Đã vô hiệu hóa người dùng"));
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Đã xóa người dùng"));
    }
}