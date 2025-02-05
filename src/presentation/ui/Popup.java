package presentation.ui;

import javax.swing.JOptionPane;

public class Popup {
	
	public static void showMessage(String msg) {
		JOptionPane.showConfirmDialog(null, msg);
	}
	
	public static void showConfirm(String msg, Popup.ConfirmListener listener) {
		int response = JOptionPane.showConfirmDialog(null, msg, "Confirm", JOptionPane.YES_NO_OPTION);
		
		if (response == JOptionPane.YES_OPTION) {
			listener.onSayYes();
		} else {
			listener.onSayNo();
		}
	}
	
	public static void showPrompt(String msg, PromptListener listener) {
		 String input = JOptionPane.showInputDialog(null, msg);
		 
		 if (input == null) {
			 listener.onCancel();
		 } else {
			 listener.onSuccess(input);
		 }
	}
	
	
	
	public interface ConfirmListener {
		public void onSayYes();
		
		public void onSayNo();
	}
	
	public interface PromptListener {
		public void onSuccess(String input);
		
		public void onCancel();
	}
}
