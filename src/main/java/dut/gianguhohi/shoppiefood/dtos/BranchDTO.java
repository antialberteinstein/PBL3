package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.misc.Address;

public class BranchDTO {
    private int branchId;
    private String branchName;
    private String phoneNumber;
    private String startTime;
    private String endTime;
    private String addressLine1;
    private String addressLine2;
    private String ward;
    private String city;

    public BranchDTO() {}

    // Convert from Branch entity to DTO
    public BranchDTO(Branch branch) {
        this.branchId = branch.getBranchId();
        this.branchName = branch.getBranchName();
        this.phoneNumber = branch.getPhoneNumber();
        this.startTime = branch.getStartTime();
        this.endTime = branch.getEndTime();
        Address address = branch.getAddress();
        if (address != null) {
            this.addressLine1 = address.getAddressLine1();
            this.addressLine2 = address.getAddressLine2();
            this.ward = address.getWard();
            this.city = address.getCity();
        }
    }

    // Getters and setters
    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}