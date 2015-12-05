package gr.ui.components.actions;

import gr.os.PodException;
import gr.os.PodManager;
import gr.ui.components.ProgressBar;
import gr.ui.controller.SharedModelController;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class SaveStatusActionListener implements ActionListener {
	private SharedModelController controller;

	public SaveStatusActionListener( SharedModelController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(MenuActions.Save.toString())) {
			Frame.getFrames()[0].setEnabled(false);
			final ProgressBar bar = new ProgressBar();
			bar.setDebugOn(controller.isDebug());
			
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground()  {
					try {
						PodManager.deletePodFiles(controller.getIpodMountPoint() ,controller.retrieveIdsToDelete(), bar);
						PodManager.addFilesToPod(controller.getIpodMountPoint() ,controller.retrieveFilesToAdd(), bar);

					} catch (PodException e) {
						bar.setVisible(false);
						JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
					}
					return null;
				};

				@Override
				protected void done() {
					super.done();
					
					try {
						controller.setGnuPodfiles(PodManager.getPodFiles(controller.getIpodMountPoint()));
						controller.resetAllModels();
					} catch (PodException e) {
						bar.setVisible(false);
						JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
					} finally {
						bar.stopProgressBar();
						Frame.getFrames()[0].setEnabled(true);
					}
				}

			};
			worker.execute();
		} else {
			try {
				controller.setGnuPodfiles(PodManager.getPodFiles(controller.getIpodMountPoint()));
				controller.resetAllModels();	
			} catch (PodException e1) {
				JOptionPane.showMessageDialog(Frame.getFrames()[0], e1.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
			
		}

	}

}
