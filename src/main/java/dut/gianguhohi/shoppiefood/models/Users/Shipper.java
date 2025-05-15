package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "shippers")
public class Shipper{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipper_id")
    private int shipperId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "driver_license", nullable = false)
    private String driverLicense;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "vehicle_type")
    private String vehicleType;

    public Shipper() {
    }

    public Shipper(User user, String driverLicense, String plateNumber, String vehicleType) {
        this.user = user;
        this.driverLicense = driverLicense;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
