package dut.gianguhohi.shoppiefood.services.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.repositories.misc.AddressRepository;
import dut.gianguhohi.shoppiefood.repositories.misc.UserAddressRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // == User Address Pane    ==
    // ==========================

    public UserAddress getUserAddressById(int id) {
        UserAddress userAddress = userAddressRepository.findByUserAddressId(id);
        if (userAddress == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Địa chỉ không tồn tại"
            );
        }
        return userAddress;
    }

    public List<UserAddress> getUserAddresses(User user) {
        if (user == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Người dùng không tồn tại"
            );
        }
        return userAddressRepository.findByUser(user);
    }

    public UserAddress getUserDefaultAddress(User user) {
        if (user == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Người dùng không tồn tại"
            );
        }
        UserAddress defaultAddress = user.getDefaultAddress();
        if (defaultAddress == null) {
            defaultAddress = user.getAddresses().stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, 
                    "Không có địa chỉ mặc định hoặc địa chỉ nào được lưu"
                ));
        }
        return defaultAddress;
    }

    public UserAddress addAddressToUser(
        User user,
        String addressLine1,
        String addressLine2,
        String ward,
        String city,
        String addressName,
        String phoneNumber,
        String note
    ) {
        Validator.validateString(ward, "Phường/xã không được để trống");
        Validator.validateString(city, "Thành phố không được để trống");

        if (user == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Người dùng không tồn tại"
            );
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            phoneNumber = user.getPhoneNumber();
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Số điện thoại không được để trống"
                );
            }
        }

        Address address = new Address(addressLine1, addressLine2, ward, city);
        address = addressRepository.save(address);
        
        UserAddress userAddress = new UserAddress(user, address, note, addressName, phoneNumber);
        
        return userAddressRepository.save(userAddress);
    }

    public UserAddress setDefault(int id, User user) {
        UserAddress userAddress = getUserAddressById(id);

        user.setDefaultAddress(userAddress);
        userRepository.save(user);

        return userAddress;
    }

    public UserAddress updateAddress(int id, String newAddressLine1, String newAddressLine2, String newWard, String newCity, String newAddressName, String newPhoneNumber, String newNote) {
        UserAddress userAddress = getUserAddressById(id);

        Address address = addressRepository.findByAddressId(userAddress.getAddress().getAddressId());
        
        Validator.validateString(newWard, "Phường/xã không được để trống");
        Validator.validateString(newCity, "Thành phố không được để trống");

        if (newCity == null || newCity.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Thành phố không được để trống"
            );
        }

        // Update address details
        address.setAddressLine1(newAddressLine1);
        address.setAddressLine2(newAddressLine2);
        address.setWard(newWard);
        address.setCity(newCity);
        
        // Update user address details
        userAddress.setAddressName(newAddressName);
        userAddress.setPhoneNumber(newPhoneNumber);
        userAddress.setNote(newNote);

        // Save changes
        addressRepository.save(address);
        return userAddressRepository.save(userAddress);
    }

    public UserAddress deleteAddress(UserAddress userAddress, User user) {

        Address address = addressRepository.findByAddressId(userAddress.getAddress().getAddressId());
        if (address == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Địa chỉ không tồn tại"
            );
        }

        userAddressRepository.delete(userAddress);

        addressRepository.delete(address);
        user.removeAddress(userAddress);

        if (user.getDefaultAddress() != null && user.getDefaultAddress().getUserAddressId() == userAddress.getUserAddressId()) {
            if (!user.getAddresses().isEmpty()) {
                user.setDefaultAddress(user.getAddresses().get(0)); // Set the first address as default if available
            } else {
                user.setDefaultAddress(null); // No addresses left, set default to null
            }
        }

        userRepository.save(user);

        // You may want to save user if needed
        return userAddress;
    }

    public UserAddress deleteAddress(int id, User user) {
        UserAddress userAddress = getUserAddressById(id);

        return deleteAddress(userAddress, user);
    }
}