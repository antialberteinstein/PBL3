package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customers extends Users {
    public Customers() {
        super();
    }

    public Customers(String userName, String password, String name, 
                    String phoneNumber, String role, boolean gender) {
        super(userName, password, name, phoneNumber, role, gender);
    }
}
