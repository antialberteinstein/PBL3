package dut.gianguhohi.shoppiefood.controller.rest.user;

import dut.gianguhohi.shoppiefood.dtos.UserAddressDTO;
import dut.gianguhohi.shoppiefood.controller.rest.user.dtos.AddressRequest;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.services.user.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-addresses")
public class UserAddressController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private HttpSession session;

    // ============================================================
    //                         TESTING ENDPOINTS
    // ============================================================

    // ==========================
    // == List User Addresses  ==
    // ==========================
    /* @GetMapping("/{userId}")
    public ResponseEntity<?> listUserAddresses(@PathVariable int userId) {
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }
        List<UserAddressDTO> addresses = addressService.getUserAddresses(user)
                .stream().map(UserAddressDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    } */

    // ==========================
    // == Add New Address      ==
    // ==========================
    /* @PostMapping("/{userId}/add")
    public ResponseEntity<?> addAddress(
            @PathVariable int userId,
            @RequestBody AddressRequest request
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress userAddress = addressService.addAddressToUser(
                user,
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getWard(),
                request.getCity(),
                request.getAddressName(),
                request.getPhoneNumber(),
                request.getNote()
        );
        return ResponseEntity.ok(new UserAddressDTO(userAddress));
    } */

    // ==========================
    // == Update Address       ==
    // ==========================
    /* @PostMapping("/{userId}/update/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable int userId,
            @PathVariable int id,
            @RequestBody AddressRequest request
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress updated = addressService.updateAddress(
                id,
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getWard(),
                request.getCity(),
                request.getAddressName(),
                request.getPhoneNumber(),
                request.getNote()
        );
        return ResponseEntity.ok(new UserAddressDTO(updated));
    } */

    // ==========================
    // == Delete Address       ==
    // ==========================
    /* @DeleteMapping("/{userId}/delete/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable int userId,
            @PathVariable int id
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress userAddress = addressService.getUserAddressById(id);
        if (userAddress == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại");
        }
        addressService.deleteAddress(userAddress, user);
        return ResponseEntity.ok(Map.of("success", true));
    } */


    // ==============================================================
    //                       REAL ENDPOINTS
    // ==============================================================

    // ==========================
    // == List User Addresses  ==
    // ==========================
    @GetMapping
    public ResponseEntity<?> listUserAddresses() {
        int userId = (int) session.getAttribute("userId");
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }
        List<UserAddressDTO> addresses = addressService.getUserAddresses(user)
                .stream().map(UserAddressDTO::new).collect(Collectors.toList());
        
        UserAddressDTO defaultAddress = new UserAddressDTO(addressService.getUserDefaultAddress(user));

        return ResponseEntity.ok(
            Map.of(
                "addresses", addresses,
                "defaultAddress", defaultAddress
            )
        );
    }

    // ==========================
    // == Add New Address      ==
    // ==========================
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(
            @RequestBody AddressRequest request
    ) {
        int userId = (int) session.getAttribute("userId");
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress userAddress = addressService.addAddressToUser(
                user,
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getWard(),
                request.getCity(),
                request.getAddressName(),
                request.getPhoneNumber(),
                request.getNote()
        );
        return ResponseEntity.ok(new UserAddressDTO(userAddress));
    }

    // ==========================
    // == Update Address       ==
    // ==========================
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable int id,
            @RequestBody AddressRequest request
    ) {
        int userId = (int) session.getAttribute("userId");
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress updated = addressService.updateAddress(
                id,
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getWard(),
                request.getCity(),
                request.getAddressName(),
                request.getPhoneNumber(),
                request.getNote()
        );
        return ResponseEntity.ok(new UserAddressDTO(updated));
    }

    // ==========================
    // == Delete Address       ==
    // ==========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable int id
    ) {
        int userId = (int) session.getAttribute("userId");
        User user = userService.readById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        UserAddress userAddress = addressService.getUserAddressById(id);
        if (userAddress == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại");
        }
        addressService.deleteAddress(userAddress, user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}