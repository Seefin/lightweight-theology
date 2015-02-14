package m3u;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


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
		// Initialize empty collections
		playlist = new ArrayList<File>();
		shuffled = false;
		// Initialize the song count
		currentSong = 1;
		// Load in the playlist from file. If it's malformed, an Exception will
		// occur.
		loadM3U(file);
		// Set up for shuffled playing.
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
		// Choose the right iterator
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
		// Sanity checking
		if (currentSong < 1 || currentSong == Integer.MAX_VALUE) {
			throw new IllegalStateException(
					"Playlist counter is invalid. It should not be: "
							+ currentSong);
		}
		// Initialize the file object
		File f = null;
		// Check if we are shuffled
		if (!shuffled) {
			// If we aren't shuffled, return the next song, index based.
			f = playlist.get(currentSong - 1);
			currentSong++;
		} else {
			// If we *are* shuffled, we need to grab the next song in the
			// shuffled list.
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
	 * Get the song at the specified index. This method is only useful for the
	 * controller if the controller is an AbstractTableModel; otherwise it is
	 * useless.
	 * 
	 * @param idx The index of the item.
	 * @return The item
	 */
	public String getSongAt(int idx) {
		return playlist.get(idx).getName();
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
	public int songCount() {
		return playlist.size();
	}

	/**
	 * Return the name of this playlist.
	 * 
	 * @return The name of this playlist as a string.
	 */
	public String getName() {
		return listName;
	}

	/**
	 * Restarts this playlist from the beginning of the list. Basically
	 * initializes the values again.
	 */
	public void resetPlaylist() {
		currentSong = 1;
		shuffleItr = shuffledList.iterator();
	}

	// -----Privates-----

	/**
	 * Calls the M3U parser and parses the file. This method extracts the
	 * filename and sets
	 * 
	 * @param list
	 */
	private void loadM3U(String list) {
		// Determine file name
		listName = getListName(list);
		// Parse M3U data
		playlist = m3u.M3UParser.parse(list);
	}

	/**
	 * Gets the name of this playlist, based on the file name. This is possibly
	 * an inefficient way of stripping the extensions, but it works FOR THIS USE
	 * CASE.
	 * 
	 * @param String
	 *            The full path to the playlist specification file
	 * @return The name of this playlist, assuming the name of the file matches
	 *         the name of the list.
	 */
	private String getListName(String path) {
		// Get path name, and grab the last bit.
		Path p = Paths.get("/var/lib/mpd/.playlist/musicList.m3u");
		String file = p.getFileName().toString();
		// Process the name, and remove any extensions.
		char[] charArray = file.toCharArray();
		int mark = charArray.length - 1;
		/*
		 * Moving backwards through the array, find the earliest occurrence of a
		 * '.' character. Anything *before* this is assumed to be the file name,
		 * and everything after is an extension.
		 */
		for (int i = mark; i > 0; i--) {
			if (charArray[i] == '.') {
				mark = i;
			}
		}
		/*
		 * Turn our character array back into a string, stopping at the point
		 * where we detected the first '.' character. This is easiest if we move
		 * forwards through the array at this point.
		 */
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < mark; i++) {
			str.append(charArray[i]);
		}
		// Return our 'sanitized' list name.
		return str.toString();
	}
}
