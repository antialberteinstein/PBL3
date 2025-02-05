package presentation.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import presentation.ui.View;

public class Home extends BaseView {
	
	public Home() {
		super("home");
		active();
		
		JLabel label = createLabel();
		panel.add(label);
		
		JButton btn = createButton();
		panel.add(btn);
	}
	
	private JLabel createLabel() {
		JLabel label = new JLabel("This is the label for Home class!");
		
		return label;
	}
	
	private JButton createButton() {
		JButton btn = new JButton("Click me for free money!");
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				View.active(new Account());
			}
		});
		
		return btn;
	}
}
