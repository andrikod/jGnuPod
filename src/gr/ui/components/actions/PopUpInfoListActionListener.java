package gr.ui.components.actions;

import gr.model.GnuPodList;
import gr.ui.controller.SharedModelController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

public class PopUpInfoListActionListener implements ActionListener {
	private JList<String> list;
	private SharedModelController model;
	private GnuPodList category;
	
	public PopUpInfoListActionListener(JList<String> list, SharedModelController model, GnuPodList category) {
		this.list = list;
		this.model = model;
		this.category = category;
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.updateAllModels(category, model.getInfoModels().get(category), list.getSelectedValuesList() );				
	}
}
