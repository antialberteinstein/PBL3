package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.misc.UserAddress;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "is_active")
    private boolean isActive;


    @OneToMany
    @JoinTable(
        name = "user_addresses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<UserAddress> addresses;

    @OneToOne
    @JoinColumn(name = "default_address_id")
    private UserAddress defaultAddress;

    @Column(name = "avatar_url")
    private String avatarUrl;

    public User() {
        this.isActive = true;
        this.defaultAddress = null;
    }

    public User(String name, String password, String phoneNumber, String email, String gender, String dateOfBirth) {
        this();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = "";
        this.password = password;
    }

    public UserAddress getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(UserAddress address) {
        this.defaultAddress = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String birthdayDate) {
        this.dateOfBirth = birthdayDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public List<UserAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<UserAddress> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(UserAddress address) {
        if (this.addresses == null) {
            this.addresses = new java.util.ArrayList<>();
        }
        this.addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(UserAddress address) {
        if (this.addresses != null) {
            this.addresses.remove(address);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
