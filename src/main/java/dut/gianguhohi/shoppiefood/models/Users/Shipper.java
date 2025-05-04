package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "shippers")
public class Shipper{
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Giấy phép lái xe không được để trống")
    @Column(name = "driver_license")
    private String driverLicense;

    @NotBlank(message = "Biển số xe không được để trống")
    @Column(name = "plate_number")
    private String plateNumber;

    @NotBlank(message = "Loại xe không được để trống")
    @Column(name = "vehicle_type")
    private int vehicleType;

    public Shipper() {
    }

    public Shipper(String driverLicense, String plateNumber, int vehicleType) {
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

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
