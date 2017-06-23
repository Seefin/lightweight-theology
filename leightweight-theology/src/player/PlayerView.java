package player;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

/**
 * Represents the view in the MVC model of the lightweight-theology application.
 * This class is the view; it is purely a GUI for the application, and holds no
 * internal logic.
 * 
 * @author Connor Findlay
 */
public class PlayerView extends JFrame {
	// Constants
	private static final String FileLabelPrefix = "Now Playing: ";
	private static final long serialVersionUID = 1L;
	// UI elements
	private JLabel filename;
	private JButton openFile, start, stop;
	private JPanel viewPanel;
	private String filenameString = "test.wav";
	private JTable playlistTable;

	/**
	 * Construct a new view. Constructs a new view object for the
	 * lightweight-theology application. Basically Initializes the User
	 * Interface.
	 */
	public PlayerView() {
		// Get the UI set up
		initUI();
	}

	/**
	 * Add an ActionListener to the start component. Adds to the actionListeners
	 * of the start button.
	 * 
	 * @param listener
	 *            The component to set as the ActionListener
	 */
	public void addPlayListener(ActionListener listener) {
		start.setActionCommand("play");
		start.addActionListener(listener);
	}

	/**
	 * Add an ActionListener to the browse component. Adds to the
	 * actionListeners of the browse button.
	 * 
	 * @param listener
	 *            The component to set as the ActionListener
	 */
	public void addBrowseListener(ActionListener listener) {
		openFile.setActionCommand("browse");
		openFile.addActionListener(listener);
	}

	/**
	 * Add an ActionListener to the stop component. Adds to the actionListeners
	 * of the stop button.
	 * 
	 * @param listener
	 *            The component to set as the ActionListener
	 */
	public void addStopListener(ActionListener listener) {
		stop.setActionCommand("stop");
		stop.addActionListener(listener);
	}

	public void addPlaylistListener(ListSelectionListener listener){
		playlistTable.getSelectionModel().addListSelectionListener(listener);
	}
	
	/**
	 * Choose a file to play. I am unsure if this should be in the model; most
	 * of it is fiddling with JFileChoosers.
	 * 
	 * @throws IOException
	 *             If we somehow select a non-existent file.
	 * @throws LineUnavailableException
	 *             If there are no sourcelines to play the file chosen.
	 * @throws UnsupportedAudioFileException
	 *             If the file chosen is not formatted correctly.
	 */
	public File browseFile() throws IOException, UnsupportedAudioFileException,
			LineUnavailableException {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter wav = new FileNameExtensionFilter("WAV Files",
				"wav", "WAV");
		FileNameExtensionFilter mp3 = new FileNameExtensionFilter("MP3 Files",
				"mp3", "MP3");
		fc.addChoosableFileFilter(wav);
		fc.addChoosableFileFilter(mp3);
		fc.setFileFilter(wav);
		int retVal = fc.showOpenDialog(null);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			if (!f.exists()) {
				throw new IOException();
			}
			updateFilenameString(f.getName());
			// Revalidates everything so the JFrame resizes to new string size.
			viewPanel.revalidate();
			this.revalidate();
			this.pack();
			return f;
		} else {
			return null;
		}
	}

	/**
	 * Set whether the file is paused or not
	 * 
	 * @param paused
	 *            true if the file is paused, false if not.
	 */
	public void isFilePaused(boolean paused) {
		if (paused) {
			start.setText("Play");
			start.setActionCommand("play");
		} else if (!paused) {
			start.setText("Pause");
			start.setActionCommand("Pause");
		} else {
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
	}

	public void setPlaylistDataModel(AbstractTableModel model) {
		playlistTable.setModel(model);
		playlistTable.getColumnModel().getColumn(0).setMaxWidth(5);
	}

	// ------ Privates ------

	/**
	 * Update the filename string. Updates the string for the filename, and then
	 * sets the JLabel text to the new filename.
	 * 
	 * @param name
	 *            The name of the currently playing file.
	 */
	private void updateFilenameString(String name) {
		// Update Now Playing: label
		filenameString = name;
		filename.setText(FileLabelPrefix + filenameString);
		// Update Window title
		setTitle("Lightweight-Theology | " + filenameString);
	}

	/**
	 * Initialize the view. This method initializes and populates the view with
	 * the buttons and fields required.
	 */
	private void initUI() {
		// Set frame default behaviors and layout
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Lightweight-Theology | " + filenameString);

		// Initialize UI elements
		filename = new JLabel(FileLabelPrefix + filenameString);
		openFile = new JButton("Browse...");
		start = new JButton("Play");
		stop = new JButton("Stop");
		playlistTable = new JTable();
		viewPanel = new JPanel(new GridBagLayout());

		// Place UI elements
		// This is complex because of the gridbaglayout, so it's in
		// it's own method.
		arrangeUIElements();
		// Add component panel
		add(viewPanel);
		// Show the view window
		setVisible(true);
		pack();
	}

	/**
	 * Populates the view. This method places each view element where it needs
	 * to go; because of the large amount of configuration needed for each
	 * element, it makes more sense in it's own method.
	 */
	private void arrangeUIElements() {
		// Initialize display area.
		GridBagConstraints gbc = new GridBagConstraints();

		// Add filename field
		gbc.anchor = GridBagConstraints.BASELINE;
		gbc.gridx = 0;
		gbc.gridy = 0;
		viewPanel.add(filename, gbc);

		// Add Browse button
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		viewPanel.add(openFile, gbc);

		// Add Play button
		gbc.gridy = 1;
		gbc.gridx = GridBagConstraints.RELATIVE;
		viewPanel.add(start, gbc);

		// Add Stop button
		gbc.gridy = 1;
		gbc.gridx = GridBagConstraints.RELATIVE;
		viewPanel.add(stop, gbc);

		// Add Playlist table
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		JScrollPane sp = setUpPlaylistTable();
		viewPanel.add(sp, gbc);
	}

	/**Sets up the playlist pane.
	 * 
	 * @return A scroll pane with the playlist in it.
	 */
	private JScrollPane setUpPlaylistTable() {
		JScrollPane sp = new JScrollPane(playlistTable);
		playlistTable.setFillsViewportHeight(true);
		playlistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistTable.setColumnSelectionAllowed(false);
		return sp;
	}

	/**
	 * For testing ONLY
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		new PlayerView();
	}
}