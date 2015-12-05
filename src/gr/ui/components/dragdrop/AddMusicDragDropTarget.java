package gr.ui.components.dragdrop;

import gr.ui.controller.SharedModelController;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class AddMusicDragDropTarget extends DropTarget {
	private static final long serialVersionUID = 1215358517725941327L;
	SharedModelController controler;
	
	public AddMusicDragDropTarget(SharedModelController controler) {
		this.controler = controler;
	}
	
	@Override
	public synchronized void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable tr = dtde.getTransferable();		
		try {
			List<File> fileList = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);
			for (File file : fileList) {
				controler.addElementToAddModel(file);						
			}			
		} catch (Exception e) {}
        
	}

}
