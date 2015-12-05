package gr.ui;

import gr.model.GnuPodList;
import gr.os.PodException;
import gr.os.PodManager;
import gr.ui.components.actions.AddFilesRemoveActionListener;
import gr.ui.components.actions.InfoListSelectionListener;
import gr.ui.components.actions.MenuActions;
import gr.ui.components.actions.PopUpInfoListActionListener;
import gr.ui.components.actions.SaveStatusActionListener;
import gr.ui.components.dragdrop.AddMusicDragDropTarget;
import gr.ui.components.popup.ListJPopupMenu;
import gr.ui.components.popup.SongsTableJPopupMenu;
import gr.ui.controller.SharedModelController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class GnuPodApp {
	
	private JFrame frame;
	private JMenuBar menuBar;	
	private SharedModelController controller;	
	
	public void createGUI() {		
		//JFrame.setDefaultLookAndFeelDecorated(true);
		
		frame = new JFrame("GnuPod");		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(700, 900));
			
		controller = new SharedModelController();
		try {
			controller.loadPodFiles();
		} catch (PodException e) {
			//JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
		addMenu();
		addFrames();		
		
		frame.pack();
		frame.setVisible(true);
		
	}
		
	public void addMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		
		JMenuItem menuItem = new JMenuItem(MenuActions.Load.toString() , KeyEvent.VK_L);
		menuItem.addActionListener(new SaveStatusActionListener( controller));
		menu.add(menuItem);
		
		menuItem = new JMenuItem(MenuActions.Save.toString(), KeyEvent.VK_S);
		menuItem.setSelected(controller.isDebug());
		menuItem.addActionListener(new SaveStatusActionListener(controller));
		menu.add(menuItem);		
		
		menu.addSeparator();
		menuItem = new JMenuItem(MenuActions.Mount.toString(), KeyEvent.VK_M);
		menuItem.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            controller.setIpodMountPoint(file.getAbsolutePath());
		        }
				
			}
		} );
		menu.add(menuItem);
		
//	   menuItem = new JMenuItem(MenuActions.Initialize.toString() , KeyEvent.VK_I);
//	   menuItem.addActionListener( new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent event) {
//				try {
//					PodManager.initializePod(controller.getIpodMountPoint());
//				} catch (PodException e) {
//					JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		} );
//	   menu.add(menuItem);
		
		
		menu.addSeparator();
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(MenuActions.Debug.toString());
		checkBox.setSelected(controller.isDebug());
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				controller.setDebug(e.getStateChange()  == ItemEvent.SELECTED);
			}
		});
		menu.add(checkBox);
		
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}
	
	public void addFrames() {		
		
		JSplitPane jSplitPaneMain = new JSplitPane( JSplitPane.VERTICAL_SPLIT, createSongsTable(), createAddMusicList());
		JSplitPane jSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,  createInfoTab(),  jSplitPaneMain);
		frame.add(jSplitPane);
		
//		JSplitPane jSplitPaneTabs = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, createInfoTab(), createInfoTab());		
//		JSplitPane jSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, jSplitPaneTabs, createSongsTable());
//		JSplitPane jSplitPaneMain = new JSplitPane( JSplitPane.VERTICAL_SPLIT, jSplitPane, createAddMusicList());
//		frame.add(jSplitPaneMain);
				
//		frame.add(createInfoTab(), BorderLayout.PAGE_START);
//		frame.add(createSongsTable(), BorderLayout.CENTER);		
//		frame.add(createAddMusicList(), BorderLayout.SOUTH);		
	}
			
	
	private JList<String> createList(GnuPodList category) {						
		DefaultListModel<String> model = controller.createModel(category);
		JList<String> list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addListSelectionListener(new InfoListSelectionListener(controller, category));
		
		PopUpInfoListActionListener listener = new PopUpInfoListActionListener(list, controller, category);
		ListJPopupMenu popup = new ListJPopupMenu(listener);
		list.setComponentPopupMenu(popup.getPopup());		
		return list;
	}
	
	private JTabbedPane createInfoTab() {
		JTabbedPane tabbedPane = new JTabbedPane();		
		
		tabbedPane.addTab("Artists", null, new JScrollPane(createList(GnuPodList.ARTIST)) , "Does nothing");
		tabbedPane.addTab("Albums", null, new JScrollPane(createList(GnuPodList.ALBUM)), "Does nothing");
		
		return tabbedPane;
	}
	
	private JScrollPane createSongsTable() {
		JTable table = new JTable(controller.createTableModel());
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(false);		
		
		SongsTableJPopupMenu popup = new SongsTableJPopupMenu(table, controller); 
		table.setComponentPopupMenu(popup.getPopup());
		
		JScrollPane scrollPane = new JScrollPane(table);				
		return scrollPane;
	}
	
	private JScrollPane createAddMusicList() {		
		JList<String> addMusicList = new JList<String>(controller.getAddModel());
		addMusicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		AddMusicDragDropTarget target = new AddMusicDragDropTarget(controller);
		addMusicList.setDragEnabled(true);
		addMusicList.setDropTarget(target);
		
		AddFilesRemoveActionListener listener = new AddFilesRemoveActionListener(addMusicList, controller);
		ListJPopupMenu popup = new ListJPopupMenu(listener);
		addMusicList.setComponentPopupMenu(popup.getPopup());
		
		JScrollPane scrollPane = new JScrollPane(addMusicList);
		return scrollPane;
	}
	
}
