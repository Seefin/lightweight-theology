package mp3;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;


public class BasicMP3FileReader extends AudioFileReader {

	@Override
	public AudioFileFormat getAudioFileFormat(InputStream stream)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioFileFormat getAudioFileFormat(URL url)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioFileFormat getAudioFileFormat(File file)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioInputStream getAudioInputStream(InputStream stream)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioInputStream getAudioInputStream(URL url)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioInputStream getAudioInputStream(File file)
			throws UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
