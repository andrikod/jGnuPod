package gr.ui.components.actions;

import gr.model.GnuPodList;
import gr.ui.controller.SharedModelController;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InfoListSelectionListener implements ListSelectionListener {
	private SharedModelController controler;
	private GnuPodList category;
	
	public InfoListSelectionListener(SharedModelController controler, GnuPodList category){
		this.controler = controler;
		this.category = category;
	}
		
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<String> list = (JList<String>) e.getSource();
		if (e.getValueIsAdjusting() == false) {
			controler.updateViewTableModel(category, list.getSelectedValuesList());	
	    }
	}
}
