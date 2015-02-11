import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


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
			} catch (Exception err) {
				err.printStackTrace();
			}
		} else if(e.getActionCommand().equalsIgnoreCase("play")){
			model.playFile();
			view.pauseFile(false);
		} else if(e.getActionCommand().equalsIgnoreCase("stop")){
			//Rewind the file
			model.stopFile();
			//Stop playback and reset play button state
			model.pauseFile();
			view.pauseFile(true);
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
