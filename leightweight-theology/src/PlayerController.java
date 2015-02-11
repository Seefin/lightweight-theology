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
		} else if(e.getActionCommand().equalsIgnoreCase("stop")){
			model.stopFile();
		} else {
			System.out.println(e.getActionCommand() + " not implemented");
		}
	}
	
	public static void main(String[] args) {
		new PlayerController();
	}

}
