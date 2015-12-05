package gr.ui.components.actions;

import gr.ui.controller.SharedModelController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

public class AddFilesRemoveActionListener implements ActionListener {
	private JList<String> list;
	private SharedModelController controler;

	public AddFilesRemoveActionListener(JList<String> list, SharedModelController controler) {
		this.list = list;
		this.controler = controler;
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		controler.removeElementToAddModel(list.getSelectedIndices());
	}

}
