package presentation.presenter.auth;

import presentation.view.auth.Login;
import business.service.UserService;

public class LoginPresenter {
	
	private Login view;
	
	public LoginPresenter() {
		this.view = new Login(this);
	}
	
	public void login(String userName, char[] password, Login.LoginListener listener) {
		try {
			boolean success = UserService.login(userName, password);
			
			if (success) {
				listener.onSuccess();
			} else {
				listener.onFailed();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			listener.onFailed();
		}
	}
	
}
