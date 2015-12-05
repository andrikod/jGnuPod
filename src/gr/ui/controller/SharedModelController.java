package gr.ui.controller;

import gr.model.GnuPodList;
import gr.os.PodException;
import gr.os.PodManager;
import gr.ui.models.SongsTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

public class SharedModelController {
	
	private String ipodMountPoint =  "/media/OZYMANDIAS\'/";
	private  boolean isDebug = true;
	
	private List<String> gnuPodfiles = new ArrayList<String>();
	private List<String> gnuPodfilesToRemove = new ArrayList<String>();
	
	private Map<GnuPodList, DefaultListModel<String>> infoModels = new HashMap<GnuPodList, DefaultListModel<String>>();
	private SongsTableModel  tableModel;

	private DefaultListModel<String> addModel = new DefaultListModel<String>();;
	private List<String> gnuPodfilesToAdd = new ArrayList<String>();
	
	public void loadPodFiles() throws PodException {
		gnuPodfiles =  PodManager.getPodFiles(ipodMountPoint);
	}
	
	public void updateViewTableModel(GnuPodList category, List<String> selectedItems) {
		List<String> newTableModel = new ArrayList<String>();
		if (selectedItems.size() == 1 && selectedItems.get(0).equals(GnuPodList.ALL) ) {
			tableModel.setData(gnuPodfiles);
			tableModel.fireTableDataChanged();
		}  else {
			for (String entry : gnuPodfiles) {
				String[] tokens = entry.split(GnuPodList.SEPARATOR);
				String infoEntry = tokens[category.getPosition()];
				if (selectedItems.contains(infoEntry)) {
					newTableModel.add(entry);
				}
				tableModel.setData(newTableModel);
				tableModel.fireTableDataChanged();
			}
		}
	}
	
	public void updateAllModels(GnuPodList category, DefaultListModel<String> model, List<String> toBeRemoved){
		extractLinesToBeRemoved(category, model, toBeRemoved);				
		updateAllModels();
	}
	
	public void updateAllModels(SongsTableModel model, int[] selectedRows) {
		extractLinesToBeRemoved(model, selectedRows);
		updateAllModels();
	}
	
	private void updateAllModels() {
		updateListModel(GnuPodList.ARTIST);
		updateListModel(GnuPodList.ALBUM);
		updateTableModel();
	}
	
	private void extractLinesToBeRemoved(SongsTableModel model, int[] selectedRows){
		List<String> toRemove = new ArrayList<String>();
		for (int index : selectedRows) {
			toRemove.add(model.getData().get(index));
		}
		gnuPodfiles.removeAll(toRemove);
		gnuPodfilesToRemove.addAll(toRemove);
	}
	
	private void extractLinesToBeRemoved(GnuPodList category, DefaultListModel<String> model, List<String> toBeRemoved){
		List<String> toRemove = new ArrayList<String>();		
		
		if ( toBeRemoved.size() == 1 && toBeRemoved.get(0) .equals(GnuPodList.ALL)  ) {
			toRemove = gnuPodfiles;
		} else {
			for (String entry : gnuPodfiles) {
				String[] tokens = entry.split(GnuPodList.SEPARATOR);
				String infoEntry = tokens[category.getPosition()];
				if (toBeRemoved.contains(infoEntry) && !infoEntry.equals(GnuPodList.ALL)) {
					toRemove.add(entry);
				}			
			}
		}
		
		gnuPodfilesToRemove.addAll(toRemove);
		gnuPodfiles.removeAll(toRemove);		
	}
	
	private void updateTableModel() {
		if (gnuPodfiles.size() > 0 ) {
			int index = 0;		
			List<Integer> toRemove = new ArrayList<Integer>();		
			for (String existedEntry : tableModel.getData()) {
				if ( !gnuPodfiles.contains(existedEntry) ) {
					toRemove.add(index);
				}
				index++;
			}
			
			int[] toRemoveIndices = new int[toRemove.size()];
			int indexRem = 0;
			for (int i : toRemove) {
				toRemoveIndices[indexRem] = i;
				indexRem++;
			}		
					
			tableModel.removeRows(toRemoveIndices);	
		} else {
			tableModel.fireTableDataChanged();
		}
				
	}
	
	private void updateListModel(GnuPodList category) {
		List<String> updatedData = PodManager.getInfoData(gnuPodfiles, category);
		DefaultListModel<String> model = infoModels.get(category);
		Enumeration<String> entries = (Enumeration<String>) model.elements();
		
		List<String> toDelete = new ArrayList<String>();
		while(entries.hasMoreElements()) {			
			String existedEntry = entries.nextElement();
			if ( !updatedData.contains(existedEntry) ) {
				toDelete.add(existedEntry);				
			}
		}
		
		for (String entry : toDelete) {
			model.removeElement(entry);
		}
		
		if (model.size() == 0 || ! model.get(0).equals(GnuPodList.ALL) ) {
			model.add(0, GnuPodList.ALL);
		}
	}		
	
	public DefaultListModel<String> createModel(GnuPodList category) {
		DefaultListModel<String> model;
		if ( infoModels.containsKey(category)  ) {
			model = infoModels.get(category);
			model.removeAllElements();
		} else {
			model = new DefaultListModel<String>();
		}
		
		model.addElement(GnuPodList.ALL);
		List<String> artists = PodManager.getInfoData(gnuPodfiles, category);
		for (String artist : artists) {
			model.addElement(artist);
		}
		
		infoModels.put(category, model);
		return model;
	}
	
	public SongsTableModel createTableModel() {
		if (tableModel == null) {
			tableModel = new SongsTableModel();
		}
		
		tableModel.setData(new ArrayList<String>(getGnuPodfiles()));
		tableModel.fireTableDataChanged();
		return tableModel;
	}		
	
	public void resetAllModels() {
		createModel(GnuPodList.ARTIST);
		createModel(GnuPodList.ALBUM);
		createTableModel();
		addModel.removeAllElements();
		gnuPodfilesToAdd = new ArrayList<String>();
		gnuPodfilesToRemove = new ArrayList<String>();
		
	}
	
	public List<String> retrieveIdsToDelete() {
		return PodManager.getInfoData(gnuPodfilesToRemove, GnuPodList.ID);
	}
	
	public List<String> retrieveFilesToAdd() {
		return gnuPodfilesToAdd;
	}
	
	public void addElementToAddModel(File file){
		if (!addModel.contains(file.getName())) {
			addModel.addElement(file.getName());
			gnuPodfilesToAdd.add( file.getAbsolutePath() );
		}
	}
	
	public void removeElementToAddModel(int[] indices){		
		Arrays.sort(indices);
		for (int i = indices.length -1; i >= 0 ; i--) {
			addModel.removeElementAt(indices[i]);
			gnuPodfilesToAdd.remove(indices[i]);
		}		
	}	
	
	public List<String> getGnuPodfiles() {
		return gnuPodfiles;
	}

	public void setGnuPodfiles(List<String> gnuPodfiles) {
		this.gnuPodfiles = gnuPodfiles;
	}

	public Map<GnuPodList, DefaultListModel<String>> getInfoModels() {
		return infoModels;
	}

	public void setInfoModels(Map<GnuPodList, DefaultListModel<String>> infoModels) {
		this.infoModels = infoModels;
	}

	public SongsTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(SongsTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public DefaultListModel<String> getAddModel() {
		return addModel;
	}

	public void setAddModel(DefaultListModel<String> addModel) {
		this.addModel = addModel;
	}

	public List<String> getGnuPodfilesToRemove() {
		return gnuPodfilesToRemove;
	}

	public void setGnuPodfilesToRemove(List<String> gnuPodfilesToRemove) {
		this.gnuPodfilesToRemove = gnuPodfilesToRemove;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getIpodMountPoint() {
		return ipodMountPoint;
	}

	public void setIpodMountPoint(String ipodMountPoint) {
		this.ipodMountPoint = ipodMountPoint ;
	}		
	
}
