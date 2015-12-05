package gr.ui.components.popup;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ListJPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = -9188747726351361685L;
	private JPopupMenu popup = new JPopupMenu();

	public ListJPopupMenu(ActionListener listener) {
		JMenuItem removeMenu = new JMenuItem("Remove");		
		removeMenu.addActionListener(listener);
		popup.add(removeMenu);
	}

	public JPopupMenu getPopup() {
		return popup;
	}
}
