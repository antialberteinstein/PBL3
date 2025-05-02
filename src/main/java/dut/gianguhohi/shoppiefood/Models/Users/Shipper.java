package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SHIPPER")
public class Shipper extends Users {
    @Column(name = "driver_license")
    private String driverLicense;

    @Column(name = "plate_number")
    private String plateNumber;

    @Column(name = "vehicle_type")
    private int vehicleType;

    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "bank_name")
    private String bankName;

    public Shipper() {
        super();
    }

    public Shipper(String userName, String password, String name, 
                  String phoneNumber, String role, boolean gender,
                  String driverLicense, String plateNumber, int vehicleType,
                  String bankNumber, String bankName) {
        super(userName, password, name, phoneNumber, role, gender);
        this.driverLicense = driverLicense;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.bankNumber = bankNumber;
        this.bankName = bankName;
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

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
