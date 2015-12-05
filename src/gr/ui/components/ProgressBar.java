package gr.ui.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgressBar extends JPanel {
	private static final long serialVersionUID = -2339965676055744182L;
	private JFrame frame;
	private JProgressBar progressBar;
	private JTextArea taskOutput;
	private boolean debugOn = true;
	private JScrollPane taskOutputPane;
	
	public ProgressBar() {
		super(new BorderLayout());
		// Create the demo's UI.
		progressBar = new JProgressBar(0, 100);
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);
		progressBar.setString("Saving..");
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
		
		JPanel panel = new JPanel();
		panel.add(progressBar);
		panel.add(taskOutput);
		
		add(panel, BorderLayout.PAGE_START);
		
		taskOutputPane = new JScrollPane(taskOutput);
        add(taskOutputPane, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		startProgressBar();
	}	

	private void startProgressBar() {
		frame = new JFrame("ProgressBarDemo");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		

		// Create and set up the content pane.
		JComponent newContentPane = this;
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);
		frame.setLocationRelativeTo(Frame.getFrames()[0]);
				
		// Display the window.
		frame.pack();
		frame.setVisible(true);		
	}	
		
	public void stopProgressBar() {
		progressBar.setIndeterminate(false);
		progressBar.setString("Saved.");
		progressBar.setValue(100);
		setCursor(null);		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	
	
	public void appendToBar(String msg) {
		if (debugOn) {
			taskOutput.append(msg + "\n");
		}
	}

	public boolean isDebugOn() {
		return debugOn;
	}

	public void setDebugOn(boolean debugOn) {
		this.debugOn = debugOn;
		//taskOutput.setVisible(debugOn);
		taskOutputPane.setVisible(debugOn);
	}
	
}
