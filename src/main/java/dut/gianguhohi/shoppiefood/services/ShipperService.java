package dut.gianguhohi.shoppiefood.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.ShipperRepository;


@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    public Shipper register(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        // Check if the user already exists in the shipper repository
        if (shipperRepository.existsByUser(user)) {
            return null; // User already exists
        }
        Shipper shipper = new Shipper(user, vehicleType, vehicleNumber, driverLicense);
        return shipperRepository.save(shipper); // Save the new shipper
    }

    public Shipper getShipperByUser(User user) {
        return shipperRepository.findByUser(user); // Find shipper by user
    }
}
