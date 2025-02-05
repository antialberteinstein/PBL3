package presentation.ui;

import javax.swing.JPanel;

public class View {
	protected String name;
	protected JPanel panel;
	private WindowViews windowViews;  // Pointer to the container.
	
	public View(String name) {
		this.name = name;
		this.panel = new JPanel();
		windowViews = null;
	}
	
	public View(String name, JPanel panel) {
		this.name = name;
		this.panel = panel;
		windowViews = null;
	}
	
	public void mapTo(WindowViews windowViews) {
		if (this.windowViews != null) {
			this.windowViews.remove(panel);
		}
		
		if (this.panel.getParent() != null) {
			this.panel.getParent().remove(panel);
			this.panel.revalidate();
			this.panel.repaint();
		}
		this.windowViews = windowViews;
		if (this.windowViews != null)
			this.windowViews.add(name, panel);
	}
	
	public void active() {
		if (this.windowViews != null)
			this.windowViews.show(name);
	}
	
	public static void active(View view) {
		if (view != null) {
			view.active();
		}
	}
}


