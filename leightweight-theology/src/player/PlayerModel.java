package player;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.*;

import m3u.Playlist;

public class PlayerModel {
	// Data
	private File audioFile;
	private AudioFormat format;
	private AudioInputStream stream;
	private SourceDataLine dataline;
	private volatile boolean stop = false;
	private volatile boolean reset = false;
	private ExecutorService es = Executors.newCachedThreadPool();
	private Playlist playlist;
	private PlayerListener listener;

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
	 * 
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public void playFile() throws IOException, UnsupportedAudioFileException,
			LineUnavailableException {
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
					listener.playerPerfomed(e);
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
	 * Set the file to a new thing. Allows dynamic changing of the file to be
	 * played by the user.
	 *
	 * @param args
	 *            File to change it to.
	 */
	public void setFilename(File file) {
		if (file != null) {
			try {
				audioFile = file;
				setupAudioSystem();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
	}

	/**
	 * Returns the duration of this file in seconds. This method returns the
	 * total duration of the current file in seconds; calling this method will
	 * tell you how long the currently loaded file is.
	 *
	 * @return duration The length of the currently load file in seconds.
	 */
	public float getFileDuration() {
		if (!audioFile.exists()) {
			return -1;
		}
		// Total file size in frames
		long fileLengthInFrames = stream.getFrameLength();
		// Frames per second.
		float fileFramesPerSecond = format.getFrameRate();
		// Total duration is Length / framerate
		float duration = fileLengthInFrames / fileFramesPerSecond;
		return duration;
	}

	/**
	 * Set up the system for playing audio. We initialize the stream, grab the
	 * format of the file,decode mp3 first, and finally grab a line so we can
	 * play the file itself.
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
		stream = AudioSystem.getAudioInputStream(audioFile);
		// Get file MIME type, to determine between WAV or MP3
		String MIMEType = Files.probeContentType(audioFile.toPath());
		if (MIMEType.matches(".*mpeg[123]*$")) {
			AudioFormat rawFormat = stream.getFormat();
			format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,// Encoding
					rawFormat.getSampleRate(), // Sample Rate
					16,// Sample size (bits)
					rawFormat.getChannels(),// channels
					rawFormat.getChannels() * 2, // frame size
					rawFormat.getSampleRate(), // frame rate
					false);// big endian?
			stream = AudioSystem.getAudioInputStream(format, stream);
		}
		/*
		 * This is not a MP3. So, it must be a WAV file, with the restrictions
		 * on File types in this application. WAV needs no decoding.
		 */
		else {
			format = stream.getFormat();
		}
		dataline = AudioSystem.getSourceDataLine(format);
	}

	public static void main(String args[]) {
		PlayerModel pm = new PlayerModel("test.wav");
		try {
			pm.playFile();
		} catch (IOException | UnsupportedAudioFileException
				| LineUnavailableException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(10000);
			pm.stopFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the name of the specified item in the playlist. If the playlist is
	 * null, (because there is no playlist loaded) then we return the name of
	 * the currently loaded song.
	 * 
	 * @param idx
	 *            Index of the item.
	 * @return The item's name.
	 */
	public String getPlaylistItem(int idx) {
		if (playlist == null) {
			return audioFile.getName();
		} else {
			return playlist.getSongAt(idx);
		}
	}

	/**
	 * Set the value of the playlist field. This is designed to accept a fully
	 * populated playlist, and place it in the playlist field.
	 * 
	 * @param p
	 *            The playlist to set as the current playlist.
	 */
	public void setPlaylist(Playlist p) {
		playlist = p;
	}

	/**
	 * Get the number of songs in the playlist. This is one if playlist is null,
	 * and larger if it is not.
	 * 
	 * @return The number of songs in the playlist.
	 */
	public int getPlaylistSongCount() {
		if (playlist == null) {
			return 1;
		} else {
			return playlist.songCount();
		}
	}

	public void setPlayerListener(PlayerListener pl) {
		listener = pl;
	}
}
