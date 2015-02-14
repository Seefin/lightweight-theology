package player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import m3u.Playlist;

public class PlayerController extends AbstractTableModel implements
ActionListener, ListSelectionListener {
	private static final long serialVersionUID = -7305209832418092647L;
	// Model and View Components.
	PlayerView view;
	PlayerModel model;

	/**
	 * Construct a new controller instance. As the controller constructs the
	 * model and view instances, this effectivly starts the program; we make
	 * this file the listener for the various buttons, then hand control off to
	 * the user.
	 */
	public PlayerController() {
		model = new PlayerModel("test.wav");
		view = new PlayerView();

		view.addStopListener(this);
		view.addPlayListener(this);
		view.addBrowseListener(this);
		view.setPlaylistDataModel(this);
		view.addPlaylistListener(this);
	}

	//-----Listeners ----
	
	public void valueChanged(ListSelectionEvent e) {
		int selectedSong = e.getLastIndex()+1;
		System.out.println("Hello! Row: "+selectedSong);
		try {
			model.stopFile();
			String name = model.getPlaylistItem(selectedSong);
			model.setFilename(new File(name));
			model.playFile();
		} catch (IOException | UnsupportedAudioFileException
				| LineUnavailableException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * ActionListener. This method is effectively the `event-driven' part of the
	 * program; when a user presses a button, the view notifies the controller
	 * via this method, and then the controller does the required action.
	 * 
	 * @param event
	 *            An event object containing information about what happened to
	 *            trigger this method.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		/*
		 * Handles the browse button. When the user presses the browse button,
		 * the controller asks the view to display a dialog and browse for the
		 * file, then tell us which file the user wants. The controller uses
		 * this to tell the model which file we now want to play. We then play
		 * the file, and notify the view that we are now in the 'playing' state.
		 */
		if (event.getActionCommand().equalsIgnoreCase("browse")) {
			try {
				File file = view.browseFile();
				if(file == null){
					return;
				}
				if(file.getName().matches(".*m3u$")){
					model.setPlaylist(new Playlist(file.getPath()));
					view.isFilePaused(true);
					model.setFilename(new File(model.getPlaylistItem(1)));
				} else {
					model.setFilename(file);
					view.isFilePaused(true);
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		/*
		 * Handle the play button. When the user presses the play button, we
		 * tell the model to play the file, and then tell the view to toggle the
		 * play/pause button to act as a pause button.
		 */
		else if (event.getActionCommand().equalsIgnoreCase("play")) {
			try {
				model.playFile();
			} catch (IOException | UnsupportedAudioFileException
					| LineUnavailableException e) {
				System.err.println("Cannot initalize audio system");
				e.printStackTrace();
			}
			view.isFilePaused(false);
		}
		/*
		 * Handle the stop button. When the user presses stop, we tell the model
		 * to pause playback (so we don't have any funny issues) and the view to
		 * reset the play/pause toggle to the play state. We then tell the model
		 * to stop the file, and reset it to the beginning.
		 */
		else if (event.getActionCommand().equalsIgnoreCase("stop")) {
			try {
				// Stop playback and reset play button state
				model.pauseFile();
				view.isFilePaused(true);
				// Rewind the file
				model.stopFile();
			} catch (IOException err) {
				System.out.println("Problem reading the selected file");
				err.printStackTrace();
			} catch (LineUnavailableException err) {
				System.out.println("The Audio System has no Line avaliable");
				err.printStackTrace();
			} catch (UnsupportedAudioFileException err) {
				System.out.println("The Audio file is not supported");
				err.printStackTrace();
			}
		}
		/*
		 * Handle the pause button. When the user presses the pause button, we
		 * tell the model to pause playback, and the view the file is no paused,
		 * so the toggle state is set correctly.
		 */
		else if (event.getActionCommand().equalsIgnoreCase("pause")) {
			model.pauseFile();
			view.isFilePaused(true);
		}
		/*
		 * Handle any other button. If we start receiving events we shouldn't,
		 * we just state the function is not implemented in this version of the
		 * player.
		 */
		else {
			System.out.println(event.getActionCommand() + " not implemented");
		}
	}

	/**
	 * Return the number of records in this playlist. This method allows us to
	 * specify the records in a playlist as rows in a {@link JTable}. Specifying
	 * the controller as a subclass of {@link AbstractTableModel} gives us this
	 * capability.
	 * 
	 * @return playlist.size(), effectively
	 */
	public int getRowCount() {
		return model.getPlaylistSongCount();
	}

	/**
	 * Get the number of columns in this playlist. This is two - we have an
	 * index and a song name.
	 * 
	 * @return 2
	 */
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Given a row and a column, what should be displayed? This is easy in the
	 * case of column 1; we display the index of an item. Column 2 is the index
	 * in the list, otherwise known as the rowIndex; so we use
	 * playlist.get(rowIndex) here.
	 * 
	 * @return Some object representing the relevant data
	 * 
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return new Integer(rowIndex + 1);
		} else if (columnIndex == 1) {
			return model.getPlaylistItem(rowIndex);
		} else {
			return "wtf";
		}
	}

	/**Returns the headers for each column in the table. 
	 * 
	 * @param col The column the header belongs to
	 * @return A string for that header.
	 */
	public String getColumnName(int col){
		if(col == 0){
			return "";
		} else if(col == 1){
			return "Title";
		} else {
			return "" + col;
		}
	}

	public static void main(String[] args) {
		new PlayerController();
	}
}
