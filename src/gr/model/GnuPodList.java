package gr.model;

public enum GnuPodList {
	ID(0),
	ARTIST(1),
	ALBUM(2),
	TITLE(3);
	
	public final static String ALL = "All";
	public final static String SEPARATOR = "\\|";
	public final static String[] columnNames = {"ARTIST", "ALBUM", "TITLE"};
	
	
	private int position;	
	
	private GnuPodList(int position) {
		this.position = position;
	}
	public int getPosition() {
		return this.position;
	}
}
