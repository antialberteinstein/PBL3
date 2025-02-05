package presentation.ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowViews {
	private JFrame window;
	private JPanel mainPanel;
	private CardLayout cardLayout;
	
	/*
	 * Create a specific Panels container for the
	 * 		given window.
	 */
	public WindowViews(JFrame window) {
		this.window = window;
		this.cardLayout = new CardLayout();
		this.mainPanel = new JPanel(this.cardLayout);
		this.window.add(mainPanel);
		
		this.window.revalidate();
		this.window.repaint();
	}
	
	/*
	 * Add a new panel into the window specified.
	 * The name given is used for presenting procedure.
	 */
	public void add(String name, JPanel panel) {
		this.mainPanel.add(panel, name);
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	
	/*
	 * Present the panel added to the window.
	 */
	public void show(String name) {
		this.cardLayout.show(mainPanel, name);
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	
	/*
	 * Remove the panel by its pointer.
	 */
	public void remove(JPanel panel) {
		this.mainPanel.remove(panel);
	}
}