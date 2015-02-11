import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class PlayerModel {
	//Data
	private File audioFile;
	private AudioFormat format;
	private AudioInputStream stream;
	private SourceDataLine dataline;

	//Calculation
	public PlayerModel(String filename){
		try {
			audioFile = new File(filename);
			if(!audioFile.exists()){throw new IOException();}
			AudioSystem.getAudioInputStream(audioFile);
			format = stream.getFormat();
			dataline = AudioSystem.getSourceDataLine(format);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void playFile(){
		byte tempBuffer[] = new byte[10000];
		try {
			dataline.open(format);
			dataline.start();
			int cnt;
			while ((cnt = stream.read(tempBuffer, 0,
					tempBuffer.length)) != -1) {
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
	
	public static void main(String args[]){
		PlayerModel pm = new PlayerModel("test.wav");
		pm.playFile();
	}
}
