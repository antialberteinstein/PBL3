package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.UserAddress;

public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private boolean enabled;
    private String avatarUrl;
    private String defaultAddress;
    private String createdAt;

    public UserDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.gender = user.getGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.enabled = user.getIsActive();
        this.avatarUrl = user.getAvatarUrl();
        UserAddress defAddr = user.getDefaultAddress();
        this.defaultAddress = (defAddr != null && defAddr.getAddress() != null)
                ? defAddr.getAddress().getFullAddress() : null;
        // If you have a createdAt field, set it here. Otherwise, remove this line.
        // this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        this.createdAt = null;
    }

    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getDefaultAddress() { return defaultAddress; }
    public void setDefaultAddress(String defaultAddress) { this.defaultAddress = defaultAddress; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}