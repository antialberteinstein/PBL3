package dut.gianguhohi.shoppiefood.modules.users.repositories;

import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.helpers.MSSQLConnector;

@Repository
public class CustomerRepository {
    public void createCustomer(int id, String name, String phoneNumber, String dob, int gender) {
        try {
            MSSQLConnector mssqlConnector = MSSQLConnector.getConnector();

            mssqlConnector.insert("customers", "" + id + ", '" + name + "', '" + phoneNumber + "', '" + dob + "', " + gender + "");

            MSSQLConnector.disconnect();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
