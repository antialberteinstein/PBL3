package presentation.ui;

import javax.swing.JFrame;

public class Window {
	private static JFrame window = null;
	private static WindowViews windowViews = null;
	
	public static final String DEFAULT_TITLE = "Default Title";
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 480;
	
	/*
	 * Get the main window of the program.
	 * Auto-init the window if it doesn't exist.
	 */
	public static JFrame getWindow() {
		if (window == null) {
			window = new JFrame(DEFAULT_TITLE);
			window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		return window;
	}
	
	/*
	 * Get the window views of the main window of the program.
	 * Auto-init if it doesn't exist.
	 */
	public static WindowViews getWindowViews() {
		if (windowViews == null) {
			windowViews = new WindowViews(getWindow());
		}
		
		return windowViews;
	}
	
	/*
	 * Create a new window for the program.
	 */
	public static JFrame init(String title, int width, int height) {
		window = new JFrame(title);
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		windowViews = new WindowViews(window);
		
		return window;
	}
	
	
}
