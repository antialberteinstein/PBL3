package dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BranchRequest {
    @NotBlank(message = "Tên chi nhánh không được để trống")
    @Size(min = 3, max = 100, message = "Tên chi nhánh phải có từ 3 đến 100 ký tự")
    private String branchName;
    
    private String phoneNumber;
    private String startTime;  // Giữ nguyên tên trường của bạn
    private String endTime;    // Giữ nguyên tên trường của bạn
    private String city;
    private String ward;
    private String addressLine1;
    private String addressLine2;
    private boolean defaultBranch; // Thêm trường này
    
    // Thêm phương thức để lấy địa chỉ đầy đủ
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine1 != null && !addressLine1.isEmpty()) {
            sb.append(addressLine1);
        }
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(addressLine2);
        }
        if (ward != null && !ward.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(ward);
        }
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        return sb.toString();
    }
    
    // Thêm các phương thức để tương thích với BranchController nếu nó sử dụng tên khác
    public String getOpenTime() {
        return startTime;
    }
    
    public void setOpenTime(String openTime) {
        this.startTime = openTime;
    }
    
    public String getCloseTime() {
        return endTime;
    }
    
    public void setCloseTime(String closeTime) {
        this.endTime = closeTime;
    }
    
    public String getAddress() {
        return getFullAddress();
    }

    // Getters and Setters hiện tại
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    
    // Getter và setter cho defaultBranch
    public boolean isDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(boolean defaultBranch) { this.defaultBranch = defaultBranch; }
}