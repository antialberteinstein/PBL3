package business.service;

import business.model.User;
import java.util.ArrayList;
import java.sql.ResultSet;
import data.MSSQLConnector;

public class UserService {
	
	public static final String table_name = "users";
	
	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			MSSQLConnector conn = MSSQLConnector.getConnector();
			
			ResultSet rs = conn.select("*", "users");
			
			while (rs.next()) {
				int id = rs.getInt(0);
				String username = rs.getString(1);
				String password = rs.getString(2);
				
				users.add(new User(id, username, password));
			}
			
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return users;
	}
	
	public static boolean login(String username, char[] password) throws Exception {
		MSSQLConnector conn = MSSQLConnector.getConnector();
		
		ResultSet rs = conn.select("*", "users", "username='" + username + "' and passwd='" + new String(password) + "'");
		
		boolean success = rs.next();
		
		rs.close();
		
		return success;
	}
}
