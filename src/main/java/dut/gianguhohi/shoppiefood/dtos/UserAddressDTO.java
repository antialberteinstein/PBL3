package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.models.misc.Address;

public class UserAddressDTO {
    private int userAddressId;
    private int userId;
    private int addressId;
    private boolean isDefault;
    private String addressName;
    private String phoneNumber;
    private String note;

    // Flattened address fields
    private String addressLine1;
    private String addressLine2;
    private String ward;
    private String city;
    private String fullAddress;

    public UserAddressDTO() {}

    public UserAddressDTO(UserAddress userAddress) {
        this.userAddressId = userAddress.getUserAddressId();
        this.userId = userAddress.getUser() != null ? userAddress.getUser().getUserId() : 0;
        Address address = userAddress.getAddress();
        if (address != null) {
            this.addressId = address.getAddressId();
            this.addressLine1 = address.getAddressLine1();
            this.addressLine2 = address.getAddressLine2();
            this.ward = address.getWard();
            this.city = address.getCity();
            this.fullAddress = address.getFullAddress();
        }
        this.isDefault = userAddress.isDefault();
        this.note = userAddress.getNote();
        this.addressName = userAddress.getAddressName();

        if (userAddress.getPhoneNumber() != null) {
            this.phoneNumber = userAddress.getPhoneNumber();
        } else {
            if (userAddress.getUser() != null && userAddress.getUser().getPhoneNumber() != null) {
                this.phoneNumber = userAddress.getUser().getPhoneNumber();
            } else {
                this.phoneNumber = "";
            }
        }
    }

    // Getters and setters
    public int getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        this.userAddressId = userAddressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}