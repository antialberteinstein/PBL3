package dut.gianguhohi.shoppiefood.controller.rest;

import dut.gianguhohi.shoppiefood.dtos.UserAddressDTO;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.services.UserService;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.repositories.misc.AddressRepository;
import dut.gianguhohi.shoppiefood.repositories.misc.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import dut.gianguhohi.shoppiefood.services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user-addresses")
public class PrepaymentRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressRepository userAddressRepository;

    // Lấy danh sách địa chỉ của user
    @GetMapping
    public ResponseEntity<?> listUserAddresses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để tiếp tục");
        }
        List<UserAddressDTO> addresses = userService.getUserAddresses(user)
                .stream().map(UserAddressDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }

    // Thêm địa chỉ mới cho user
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam String ward,
            @RequestParam String city,
            @RequestParam String addressName,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String note,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để tiếp tục");
        }
        // Refresh user from database to ensure we have the latest data
        user = userService.readById(user.getUserId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        session.setAttribute("user", user);

        UserAddress userAddress = userService.addAddressToUser(
                user, addressLine1, addressLine2, ward, city, addressName, phoneNumber, note
        );
        return ResponseEntity.ok(new UserAddressDTO(userAddress));
    }

    // Cập nhật địa chỉ
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable int id,
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam String ward,
            @RequestParam String city,
            @RequestParam String addressName,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String note
    ) {
        UserAddress address = userAddressRepository.findByUserAddressId(id);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại");
        }
        
        UserAddress updated = userService.updateAddress(
                address.getUser(), address.getAddress(), addressLine1, addressLine2, ward, city, addressName, phoneNumber, note
        );
        return ResponseEntity.ok(new UserAddressDTO(updated));
    }

    // Xóa địa chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable int id
    ) {
        UserAddress address = userAddressRepository.findByUserAddressId(id);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại");
        }
        userService.deleteAddress(address.getUser(), address.getAddress());
        return ResponseEntity.ok().body("Đã xóa địa chỉ");
    }
}