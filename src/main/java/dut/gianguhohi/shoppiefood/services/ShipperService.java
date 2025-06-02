package dut.gianguhohi.shoppiefood.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.ShipperRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Transactional
@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    // ==========================
    // == Register Shipper     ==
    // ==========================
    public Shipper register(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        validateRegister(user, vehicleType, vehicleNumber, driverLicense);

        if (shipperRepository.existsByUser(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng đã đăng ký làm shipper");
        }

        Shipper shipper = new Shipper(user, vehicleType, vehicleNumber, driverLicense);
        return shipperRepository.save(shipper);
    }

    // ==========================
    // == Get Shipper by User  ==
    // ==========================
    public Shipper getShipperByUser(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không hợp lệ");
        }
        Shipper shipper = shipperRepository.findByUser(user);
        if (shipper == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng này không phải là shipper");
        }
        return shipper;
    }

    // ==========================
    // == Validation Methods   ==
    // ==========================
    private void validateRegister(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không hợp lệ");
        }
        Validator.validateString(vehicleType, "Loại xe không được để trống");
        Validator.validateString(vehicleNumber, "Biển số xe không được để trống");
        Validator.validateString(driverLicense, "Giấy phép lái xe không được để trống");
    }
}