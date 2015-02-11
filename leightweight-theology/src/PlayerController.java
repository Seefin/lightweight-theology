
public class PlayerController {
	PlayerView view;
	PlayerModel model;
	
	public PlayerController(){
		model = new PlayerModel("test.wav");
		view = new PlayerView();
		
		view.addStopListener(model);
		view.addPlayListener(model);
		view.addBrowseListener(model);
	}
	
	public static void main(String[] args) {
		new PlayerController();
	}

}
