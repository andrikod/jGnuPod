package gr.ui.components.popup;

import gr.ui.components.actions.PopUpTableActionListener;
import gr.ui.controller.SharedModelController;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class SongsTableJPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = -7367531618161568878L;
	private JPopupMenu popup = new JPopupMenu();
	
	public SongsTableJPopupMenu(JTable table, SharedModelController model) {
		JMenuItem removeMenu = new JMenuItem("Remove");
		PopUpTableActionListener listener = new PopUpTableActionListener(table, model);		
		removeMenu.addActionListener(listener);
        popup.add(removeMenu);               
	}	
	
	public JPopupMenu getPopup() {
		return popup;
	}	
}
