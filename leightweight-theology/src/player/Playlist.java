package player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

/**
 * Represents a playlist of music. Provides storage for a playlist of files,
 * parsing m3u playlists, and what song is next in the list.
 * 
 * @author Connor Findlay
 */
public class Playlist {
	private ArrayList<File> playlist;
	private volatile boolean shuffled;
	private String listName;
	private volatile int currentSong;
	private ArrayList<File> shuffledList;
	private Iterator<File> shuffleItr;

	/**
	 * Create a new playlist object, with the contents of the specified file. We
	 * cannot, as yet, create empty playlists. We also cannot store more than
	 * (2^31)-1 songs in any given list.
	 * 
	 * @param file
	 *            The M3U file this playlist represents.
	 * @throws IllegalArgumentException
	 *             If the specified file is not a valid M3U file.
	 */
	public Playlist(String file) throws IllegalArgumentException {
		playlist = new ArrayList<File>();
		shuffled = false;
		currentSong = 1;
		loadM3U(file);
		shuffledList = new ArrayList<File>(playlist);
		Collections.shuffle(shuffledList);
		shuffleItr = shuffledList.iterator();
	}

	/**
	 * Return an iterator over this list. This method can be used to get an
	 * iterator over this playlist. This is *not* recommended behavior, but it
	 * may be useful. However, if used, the internal iterator the other methods
	 * in this class use will not be consistent with the one returned by this
	 * method. (I think). If the playlist is currently shuffled, you will get an
	 * iterator over the shuffled collection.
	 * 
	 * @return An Iterator over this playlist.
	 */
	public Iterator<File> getIterator() {
		if (!shuffled)
			return playlist.iterator();
		return shuffleItr;
	}

	/**
	 * Returns the path to the next file. This method returns the canonical path
	 * to the next song in the playlist.
	 * 
	 * @return The canonical path of the next file in the list.
	 * @throws IOException
	 *             If the file doesn't actually exist.
	 */
	public String nextFile() throws IOException {
		if (currentSong < 1 || currentSong == Integer.MAX_VALUE) {
			throw new IllegalStateException(
					"Playlist counter is invalid. It should not be: "
							+ currentSong);
		}
		File f = null;
		if (!shuffled) {
			f = playlist.get(currentSong - 1);
			currentSong++;
		} else {
			f = shuffleItr.next();
			currentSong++;
		}
		return f.getCanonicalPath();
	}

	/**
	 * Set the shuffle status of this list to the specified boolean. If true,
	 * the list is shuffled. If false, it isn't.
	 * 
	 * @param status
	 *            The answer to the question, is this list shuffled?
	 */
	public void setShuffleStatus(boolean status) {
		shuffled = status;
	}

	/**
	 * Show progress through the list. This method returns the index of the
	 * current song in the list.
	 * 
	 * @return The index of the current song in the playlist. This index is
	 *         1-based.
	 */
	public int getCurrentSong() {
		return currentSong;
	}

	/**
	 * Add a file to the playlist. Not yet supported.
	 * 
	 * @param f
	 *            The file to add to the list.
	 */
	public void add(File f) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove a file from the list. Not yet supported.
	 * 
	 * @param f
	 *            The file to be removed from the list.
	 */
	public void remove(File f) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the number of songs in the list.
	 * 
	 * @return The number of songs in this playlist. Again, this is a 1-based
	 *         answer.
	 */
	public int songCount(){
		return playlist.size();
	}

	// -----Privates-----

	private boolean loadM3U(String list) {
		return false;
	}
}
