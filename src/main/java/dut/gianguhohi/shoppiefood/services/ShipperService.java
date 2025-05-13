package dut.gianguhohi.shoppiefood.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.ShipperRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShipperService extends BaseService<Shipper, Integer> {

    @Autowired
    private ShipperRepository shipperRepository;

    @Override
    protected ShipperRepository getRepository() {
        return shipperRepository;
    }

    public Shipper register(User user, String vehicleType, String vehicleNumber, String driverLicense) {
        validateEntity(user);
        
        if (shipperRepository.existsByUser(user)) {
            throw new ShipperRelatedException("User is already registered as a shipper");
        }

        if (shipperRepository.existsByVehicleNumber(vehicleNumber)) {
            throw new ShipperRelatedException("Vehicle number is already registered");
        }

        if (shipperRepository.existsByDriverLicense(driverLicense)) {
            throw new ShipperRelatedException("Driver license is already registered");
        }

        Shipper shipper = new Shipper(user, vehicleType, vehicleNumber, driverLicense);
        shipper.setActive(true);
        return save(shipper);
    }

    public Optional<Shipper> getShipperByUser(User user) {
        return Optional.ofNullable(shipperRepository.findByUser(user));
    }

    public List<Shipper> getAvailableShippers() {
        return shipperRepository.findByActiveTrue();
    }

    public Shipper update(Integer id, Shipper shipper) {
        validateEntity(shipper);
        Shipper existingShipper = findById(id)
            .orElseThrow(() -> new ShipperRelatedException("Shipper not found"));

        // Check for unique constraints
        if (!existingShipper.getVehicleNumber().equals(shipper.getVehicleNumber()) && 
            shipperRepository.existsByVehicleNumber(shipper.getVehicleNumber())) {
            throw new ShipperRelatedException("Vehicle number is already registered");
        }

        if (!existingShipper.getDriverLicense().equals(shipper.getDriverLicense()) && 
            shipperRepository.existsByDriverLicense(shipper.getDriverLicense())) {
            throw new ShipperRelatedException("Driver license is already registered");
        }

        existingShipper.setVehicleType(shipper.getVehicleType());
        existingShipper.setVehicleNumber(shipper.getVehicleNumber());
        existingShipper.setDriverLicense(shipper.getDriverLicense());
        
        return save(existingShipper);
    }

    public Shipper toggleStatus(Integer id) {
        Shipper shipper = findById(id)
            .orElseThrow(() -> new ShipperRelatedException("Shipper not found"));
        
        shipper.setActive(!shipper.isActive());
        return save(shipper);
    }

    public void delete(Integer id) {
        Shipper shipper = findById(id)
            .orElseThrow(() -> new ShipperRelatedException("Shipper not found"));
        delete(shipper);
    }

    public static class ShipperRelatedException extends RuntimeException {
        public ShipperRelatedException(String message) {
            super(message);
        }
    }
}
