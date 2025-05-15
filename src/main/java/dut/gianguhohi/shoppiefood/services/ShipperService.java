package dut.gianguhohi.shoppiefood.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.ShipperRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.utitls.AppServiceException;
import dut.gianguhohi.shoppiefood.utitls.Validator;

@Transactional
@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    public Shipper register(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        validateRegister(user, vehicleType, vehicleNumber, driverLicense);

        if (shipperRepository.existsByUser(user)) {
            throw new RegisterRelatedException("Người dùng đã đăng ký làm shipper");
        }

        Shipper shipper = new Shipper(user, vehicleType, vehicleNumber, driverLicense);
        return shipperRepository.save(shipper);
    }

    public Shipper getShipperByUser(User user) {
        if (user == null) {
            throw new AppServiceException("Người dùng không hợp lệ");
        }
        Shipper shipper = shipperRepository.findByUser(user);
        if (shipper == null) {
            throw new AppServiceException("Người dùng này không phải là shipper");
        }
        return shipper;
    }

    // Validation methods
    private void validateRegister(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        if (user == null) {
            throw new AppServiceException("Người dùng không hợp lệ");
        }
        Validator.validateString(vehicleType, "Loại xe không được để trống");
        Validator.validateString(vehicleNumber, "Biển số xe không được để trống");
        Validator.validateString(driverLicense, "Giấy phép lái xe không được để trống");
    }
}