package dut.gianguhohi.shoppiefood.models.misc;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Entity
@Table(name = "user_addresses")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_address_id")
    private int userAddressId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "address_name")
    private String addressName;
    
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "note")
    private String note;

    public UserAddress() {}

    public UserAddress(User user, Address address, String note, String addressName, String phoneNumber) {
        this.user = user;
        this.address = address;
        this.note = note;
        this.addressName = addressName;
        this.phoneNumber = phoneNumber;
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

    public int getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        this.userAddressId = userAddressId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}