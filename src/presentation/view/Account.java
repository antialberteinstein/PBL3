package presentation.view;

import javax.swing.JLabel;

public class Account extends BaseView {
	
	public Account() {
		super("account");
		
		JLabel label = createLabel();
		panel.add(label);
	}
	
	private JLabel createLabel() {
		JLabel label = new JLabel("This is the label for Account class!");
		
		return label;
	}
}
