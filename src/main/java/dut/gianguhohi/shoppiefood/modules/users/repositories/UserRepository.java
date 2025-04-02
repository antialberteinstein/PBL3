package dut.gianguhohi.shoppiefood.modules.users.repositories;

import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.helpers.MSSQLConnector;
import java.sql.ResultSet;

@Repository
public class UserRepository {

    // Return user's id.
    // Return -1 if user not found.
    public int validateUser(String userName, String password) {
        try {
            MSSQLConnector mssqlConnector = MSSQLConnector.getConnector();
            
            ResultSet users =
                mssqlConnector.select("*", "users",
                "username = '" + userName + "' AND passwd = '" + password + "'");
            int userId = -1;
            if (!users.next()) {
                return -1;
            } else {
                userId = users.getInt("id");
            }

            MSSQLConnector.disconnect();

            return userId;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }

    }

    // Return the id of the created user
    public int createUser(String userName, String password) {
        try {
            MSSQLConnector mssqlConnector = MSSQLConnector.getConnector();
            
            mssqlConnector.insert("users", "username, passwd",
                "'" + userName + "', '" + password + "'");
            
            // Get the id of the created user
            ResultSet users =
                mssqlConnector.select("id", "users",
                "username = '" + userName + "'");
            
            if (!users.next()) {
                MSSQLConnector.disconnect();
                return -1;
            } else {
                int id = users.getInt("id");
                MSSQLConnector.disconnect();
                return id;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public boolean isUserExist(String userName) {
        try {
            MSSQLConnector mssqlConnector = MSSQLConnector.getConnector();
            
            ResultSet users =
                mssqlConnector.select("*", "users",
                "username = '" + userName + "'");
            
            if (!users.next()) {
                return false;
            }

            MSSQLConnector.disconnect();

            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
