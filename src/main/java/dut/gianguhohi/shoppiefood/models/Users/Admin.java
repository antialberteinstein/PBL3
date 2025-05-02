package dut.gianguhohi.shoppiefood.models.Users;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Users {
    public Admin() {
        super();
    }

    public Admin(String userName, String password, String name, 
                String phoneNumber, String role, boolean gender) {
        super(userName, password, name, phoneNumber, role, gender);
    }
}
