import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class PlayerModel implements ActionListener{
	//Data
	private File audioFile;
	private AudioFormat format;
	private AudioInputStream stream;
	private SourceDataLine dataline;
	private volatile boolean stop = false; 
	private ExecutorService es = Executors.newCachedThreadPool();

	//Calculation
	public PlayerModel(String filename){
		try {
			audioFile = new File(filename);
			if(!audioFile.exists()){System.out.println("Not exist!");throw new IOException();}
			stream = AudioSystem.getAudioInputStream(audioFile);
			format = stream.getFormat();
			dataline = AudioSystem.getSourceDataLine(format);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void playFile(){
		stop = false;
		byte tempBuffer[] = new byte[10000];
		Runnable playTask = new Runnable(){
			public void run(){
				try {
					dataline.open(format);
					dataline.start();
					int cnt;
					while ((cnt = stream.read(tempBuffer, 0,
							tempBuffer.length)) != -1 && !stop) {
						if (cnt > 0) {
							dataline.write(tempBuffer, 0, cnt);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dataline.drain();
					dataline.close();
				}
			}
		};
		es.execute(playTask);
	}

	public void stopFile(){
		stop = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("browse")){
			System.out.println("Browsing not implemented yet!");
		} else if(e.getActionCommand().equalsIgnoreCase("play")){
			System.out.println("Play!");
			playFile();
		} else if(e.getActionCommand().equalsIgnoreCase("stop")){
			System.out.println("Stop!");
			stopFile();
		} else {
			System.out.println(e.getActionCommand() + " not implemented");
		}
	}

	public static void main(String args[]){
		PlayerModel pm = new PlayerModel("test.wav");
		pm.playFile();
		try{
			Thread.sleep(3000);
		} catch(Exception e){
			e.printStackTrace();
		}
		pm.stopFile();
	}


}
