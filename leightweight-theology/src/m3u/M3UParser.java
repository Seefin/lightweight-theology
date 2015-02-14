package m3u;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.m3u.M3U;
import christophedelory.playlist.m3u.Resource;

public class M3UParser {
	/**
	 * Parse an M3UList and return a list of File objects associated with the
	 * resources in the list.
	 * 
	 * @param file The M3U file to be parsed
	 * @return A list of the File objects in the M3U file.
	 */
	public static ArrayList<File> parse(String file) {
		File listAsFile = new File(file);
		SpecificPlaylist specificList = null;
		M3U m3uList = null;
		try {
			specificList = SpecificPlaylistFactory.getInstance().readFrom(
					listAsFile);
			m3uList = (M3U) specificList;
		} catch (IOException e) {
			System.err.println("Playlist parser encountered malformed data");
			e.printStackTrace();
		}
		ArrayList<File> fileList = new ArrayList<File>();
		for (Resource r : m3uList.getResources()) {
			fileList.add(new File(r.getLocation()));
		}
		return fileList;
	}
}
