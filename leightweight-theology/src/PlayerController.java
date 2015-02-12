import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class PlayerController implements ActionListener{
	PlayerView view;
	PlayerModel model;

	public PlayerController(){
		model = new PlayerModel("test.wav");
		view = new PlayerView();

		view.addStopListener(this);
		view.addPlayListener(this);
		view.addBrowseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("browse")){
			try {
				File file = view.browseFile();
				model.setFilename(file);
				view.pauseFile(true);
			} catch (Exception err) {
				err.printStackTrace();
			}
		} else if(e.getActionCommand().equalsIgnoreCase("play")){
			model.playFile();
			view.pauseFile(false);
		} else if(e.getActionCommand().equalsIgnoreCase("stop")){
			try{
				//Rewind the file
				model.stopFile();
				//Stop playback and reset play button state
				model.pauseFile();
				view.pauseFile(true);
			} catch (IOException err) {
				System.out.println("Problem reading the selected file");
				err.printStackTrace();
			} catch (LineUnavailableException err){
				System.out.println("The Audio System has no Line avaliable");
				err.printStackTrace();
			} catch (UnsupportedAudioFileException err){
				System.out.println("The Audio file is not supported");
				err.printStackTrace();
			}
		} else if(e.getActionCommand().equalsIgnoreCase("pause")){
			model.pauseFile();
			view.pauseFile(true);
		} else {
			System.out.println(e.getActionCommand() + " not implemented");
		}
	}

	public static void main(String[] args) {
		new PlayerController();
	}

}
