package presentation.presenter.test;

import data.MSSQLConnector;
import java.sql.ResultSet;
import presentation.presenter.auth.LoginPresenter;
import presentation.ui.*;
import presentation.view.*;

public class TestClass {

	public static void main(String[] args) {
		testLoginProcedure();
	}
	
	public static void testUI() {
		Window.init("Shoppie", 640, 480).setVisible(true);
		
		new Home();
	}
	
	public static void testLoginProcedure() {
		Window.init("Shoppie", 640, 480).setVisible(true);
		
		new LoginPresenter();
	}
	
	public static void testDatabaseConnection() {
		// Testing database connection.
		try {
			System.out.println("Starting to connect to sql server...");
			MSSQLConnector connector = MSSQLConnector.getConnector();
			
			ResultSet rs = connector.select("*", "users");
			
			while (rs.next()) {
				String userName = rs.getString("userName");
				String password = rs.getString("passwd");
				
				System.out.println("|" + userName + "|" + password);
			}
			
			rs.close();
			
			System.out.println("Successful!");
			
			MSSQLConnector.disconnect();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
