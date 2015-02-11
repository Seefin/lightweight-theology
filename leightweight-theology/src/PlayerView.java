import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.*;
public class PlayerView extends JFrame {
	private static final long serialVersionUID = 1L;
	JTextField filename;
	JButton openFile,stop,start;
	JPanel viewPanel;
	
	public static void main(String args[]){
		new PlayerView();
	}

	public PlayerView(){
		//Get the UI set up
		initUI();
		
		//Show the view window
		setVisible(true);
	}

	/**Add an ActionListener to the stop component.
	 * Adds to the actionListeners of the stop button.
	 * @param listener The component to set as the ActionListener
	 */
	public void addStopListener(ActionListener listener){
		stop.addActionListener(listener);
	}
	
	/**Add an ActionListener to the start component.
	 * Adds to the actionListeners of the start button.
	 * @param listener The component to set as the ActionListener
	 */
	public void addPlayListener(ActionListener listener){
		start.addActionListener(listener);
	}
	
	/**Add an ActionListener to the browse component.
	 * Adds to the actionListeners of the browse button.
	 * @param listener The component to set as the ActionListener
	 */
	public void addBrowseListener(ActionListener listener){
		openFile.addActionListener(listener);
	}
	
	//------ Privates ------
	
	/**Initialize the view.
	 * This method initializes and populates the view with the
	 * buttons and fields required.
	 */
	private void initUI() {
		//Set frame default behaviors and layout
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 200);
		
		//Initialize UI elements
		filename = new JTextField(20);
		openFile = new JButton("Browse...");
		stop = new JButton("Stop");
		start = new JButton("Play");
		
		
		//Place UI elements
		// This is complex because of the gridbaglayout, so it's in
		// it's own method.
		arrangeUIelements();
		
		add(viewPanel);
	}

	/**Populates the view.
	 * This method places each view element where it needs to go;
	 * because of the large amount of configuration needed for each element,
	 * it makes more sense in it's own method.
	 */
	private void arrangeUIelements() {
		//Initialize display area.
		GridBagConstraints gbc = new GridBagConstraints();
		viewPanel = new JPanel(new GridBagLayout());
		
		//Add filename field
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.5;
		viewPanel.add(filename, gbc);

		//Add Browse button
		gbc.gridx = 1;
		gbc.weightx = 0.2;
		viewPanel.add(openFile, gbc);
		
		//Add Play button
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.weightx = 0.1;
		viewPanel.add(start, gbc);
		
		//Add Stop button
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		viewPanel.add(stop, gbc);
	}
}
