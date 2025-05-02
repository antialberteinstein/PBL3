package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.misc.Address;

@Entity
@DiscriminatorValue("SELLER")
public class Seller extends Users {
    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "bank_name")
    private String bankName;

    public Seller() {
        super();
    }

    public Seller(String userName, String password, String name, 
                 String phoneNumber, String role, boolean gender,
                 String shopName, String bankNumber, String bankName) {
        super(userName, password, name, phoneNumber, role, gender);
        this.shopName = shopName;
        this.bankNumber = bankNumber;
        this.bankName = bankName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
