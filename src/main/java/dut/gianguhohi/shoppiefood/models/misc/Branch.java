package dut.gianguhohi.shoppiefood.models.misc;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import dut.gianguhohi.shoppiefood.models.Users.Seller;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private int branchId;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Branch() {
        this.createdAt = LocalDateTime.now();
    }

    public Branch(Seller seller, Address address, String branchName) {
        this.seller = seller;
        this.address = address;
        this.branchName = branchName;
        this.createdAt = LocalDateTime.now();
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
