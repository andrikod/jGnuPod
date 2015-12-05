package gr.ui.models;

import gr.model.GnuPodList;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class SongsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1438825566949516748L;
	private String[] columnNames = GnuPodList.columnNames;
	private List<String> data ;
	
	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}	
	
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}	
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String row = data.get(rowIndex);
		String[] tokens = row.split(GnuPodList.SEPARATOR);
		return tokens[++columnIndex];
	}
	
	public void removeRows(int[] rows) {	
		Arrays.sort(rows);
		for (int i = rows.length-1; i >= 0; i--) {			
			data.remove(rows[i]);
			fireTableRowsDeleted(rows[i], rows[i]);
		}			
    }	
}
