import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayerModel {
	// Data
	private File audioFile;
	private AudioFormat format;
	private volatile BufferedInputStream bis;
	private AudioInputStream stream;
	private SourceDataLine dataline;
	private volatile boolean stop = false;
	private volatile boolean reset = false;
	private ExecutorService es = Executors.newCachedThreadPool();

	// Calculation
	/**
	 * Creates a new Model. The created model has all of the audio system set up
	 * and ready to go, and just needs to be supplied with an audio file so it
	 * can play it.
	 * 
	 * @param filename
	 *            The filename of the audio file to play.
	 */
	public PlayerModel(String filename) {
		try {
			audioFile = new File(filename);
			setupAudioSystem();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play the selected file. Plays the selected file. It does this by wrapping
	 * all the 'play stream' logic in an anonymous instance of Runnable, and
	 * uses java's concurrency libraries to get it running.
	 * 
	 * As the stop boolean is volatile, it will be monitored even in a multiple
	 * core environment.
	 */
	public void playFile() {
		// Set up for play back
		stop = false;
		reset = false;
		byte tempBuffer[] = new byte[10000];
		// Wrap play logic in a runnable
		Runnable playTask = new Runnable() {
			public void run() {
				try {
					dataline.open(format);
					dataline.start();
					int cnt = 0;
					while ((cnt = stream.read(tempBuffer, 0, tempBuffer.length)) != -1
							&& !stop) {
						if (reset) {
							System.out.println("reset == true");
							return;
						}
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
		// Run the runnable somewhere asynchronously.
		es.execute(playTask);
	}

	/**
	 * Stops playback of the current file.
	 * 
	 */
	public void pauseFile() {
		stop = true;
	}

	/**
	 * Resets the file to the beginning. This method is effectively a 'rewind'
	 * method. It resets the stream to the beginning, and then plays the stream.
	 * 
	 * @throws LineUnavailableException
	 *             If we can't get an Audio Line
	 * @throws UnsupportedAudioFileException
	 *             The Audio file is *wrong*
	 * @throws IOException
	 *             There was an IO related error in reading the file
	 */
	public void stopFile() throws IOException, UnsupportedAudioFileException,
			LineUnavailableException {
		// Signal to play back mechanism we want to not play back
		reset = true;
		// Reset back to the intial state
		setupAudioSystem();
	}

	/**
	 * Set up the system for playing audio. We initialize the stream, grab the
	 * format of the file (should be 16-bit signed Low-endian PCM wav), and
	 * finally grab a line so we can play the file itself.
	 * 
	 * @throws IOException
	 *             If the file is non-existent
	 * @throws UnsupportedAudioFileException
	 *             If the format is not supported on this hardware
	 * @throws LineUnavailableException
	 *             If there are no source lines for the program to use.
	 */
	private void setupAudioSystem() throws IOException,
			UnsupportedAudioFileException, LineUnavailableException {
		if (!audioFile.exists()) {
			System.out.println("Not exist!");
			throw new IOException();
		}
		// To enable marking and reseting the stream, we wrap in a buffered
		// stream.
		bis = new BufferedInputStream(new FileInputStream(audioFile));
		stream = AudioSystem.getAudioInputStream(bis);
		format = stream.getFormat();
		dataline = AudioSystem.getSourceDataLine(format);
	}

	/**
	 * Set the file to a new thing! Allows dynamic changing of the file to be
	 * played by the user.
	 * 
	 * @param args
	 *            File to change it to.
	 */
	public void setFilename(File file) {
		if (file != null) {
			try {
				stopFile();
				audioFile = file;
				setupAudioSystem();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
	}

	public static void main(String args[]) {
		PlayerModel pm = new PlayerModel("test.wav");
		pm.playFile();
		try {
			Thread.sleep(3000);
			pm.stopFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
