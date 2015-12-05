package gr.ui;



public class MainWindow {

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {            	
            	new GnuPodApp().createGUI();            
            }
        });
    }

}
