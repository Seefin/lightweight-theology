import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayerController implements ActionListener {
	//Model and View Components.
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
	}

	/**
	 * ActionListener. This method is effectivly the `event-driven' part of the
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
				model.setFilename(file);
				view.isFilePaused(true);
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

	public static void main(String[] args) {
		new PlayerController();
	}

}
