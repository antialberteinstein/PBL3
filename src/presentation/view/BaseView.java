package presentation.view;

import presentation.ui.View;
import presentation.ui.Window;

public class BaseView extends View {
	
	public BaseView(String name) {
		super(name);
		mapTo(Window.getWindowViews());
	}
}
