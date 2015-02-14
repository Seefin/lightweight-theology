package mp3;

import javax.sound.sampled.AudioFormat;

public class BasicMP3Encoding extends AudioFormat.Encoding{
	public static final AudioFormat.Encoding MP3 = new BasicMP3Encoding("MP3");
	public BasicMP3Encoding(String name) {
		super(name);
	}

}
