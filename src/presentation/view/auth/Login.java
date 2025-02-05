package presentation.view.auth;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import presentation.ui.Window;
import presentation.view.BaseView;
import presentation.presenter.auth.LoginPresenter;

public class Login extends BaseView {
	
	private LoginPresenter presenter;
	
	public Login(LoginPresenter presenter) {
		super("login");
		
		this.presenter = presenter;
		
		createLoginFields();
		
		active();
	}
	
	private void createLoginFields() {
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Create constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        // Add username text field
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Add password label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        // Add password field
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Add login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton.setBackground(new Color(34, 167, 240));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                if (username.isEmpty() || password.length == 0) {
                    JOptionPane.showMessageDialog(Window.getWindow(), "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    
                    presenter.login(username, password, new LoginListener() {
                    	
                    	@Override
                    	public void onSuccess() {
                    		JOptionPane.showMessageDialog(Window.getWindow(), "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    	}
                    	
                    	@Override
                    	public void onFailed() {
                    		JOptionPane.showMessageDialog(Window.getWindow(), "Login failed!",
                    				"Error", JOptionPane.ERROR_MESSAGE);
                    	}
                    	
                    });
                }
            }
        });
	}
	
	public interface LoginListener {
		public void onSuccess();
		
		public void onFailed();
	}
}
