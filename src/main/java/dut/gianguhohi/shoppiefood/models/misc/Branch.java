package dut.gianguhohi.shoppiefood.models.misc;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private int branchId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    public Branch() {
    }

    public Branch(Restaurant restaurant, Address address, String branchName) {
        this.restaurant = restaurant;
        this.address = address;
        this.branchName = branchName;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
