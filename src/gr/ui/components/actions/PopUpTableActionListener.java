package gr.ui.components.actions;

import gr.ui.controller.SharedModelController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

public class PopUpTableActionListener implements ActionListener {
	private JTable table;
	private SharedModelController model;
	
	public PopUpTableActionListener(JTable table, SharedModelController model) {
		this.table = table;
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.updateAllModels(model.getTableModel(), table.getSelectedRows());	
	}
}
