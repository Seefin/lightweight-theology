package core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Streamer;

public class InetStreamer implements Streamer {
	private Map<String, File> songs = new HashMap<String,File>();
	private File currentSongFile;
	private String currentSongName;
	
	public InetStreamer(File inputFile) throws Exception {
		System.out.println("Streamer serving files from: " + inputFile.getName());
		importSongDatabase(inputFile);
		assert(songs.size() > 0);
	}

	@Override
	public void serveMusic(int port) {
		for(String name : songs.keySet()){
			currentSongFile = songs.get(name);
			currentSongName = name;
			File song = getSongFile();
			assert(song != null);
			try(ServerSocket serverSocket = new ServerSocket(port); FileInputStream in = new FileInputStream(song)) {
				if(serverSocket.isBound()){
					Socket client = serverSocket.accept();
					OutputStream output = client.getOutputStream();
					
					byte buffer[] = new byte[1024];
					int count = 0;
					while((count = in.read(buffer)) != -1){
						output.write(buffer,0,count);
					}
				}
			} catch (Exception e) {
				System.out.println("Something Happened!");
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());
				System.exit(1);
			}
		}
		System.out.println("Streamer shutting down");
	}

	public String getSongName(){
		return currentSongName;
	}
	
	public File getSongFile(){
		return currentSongFile;
	}
	
	private void importSongDatabase(File inputFile) throws Exception {
		assert(inputFile != null);
		String name = "";
		File songFile;
		
		FileInputStream fis = new FileInputStream(inputFile);
		Reader fr = new InputStreamReader(fis);
		
		List<String> line = core.util.CSVParser.parseLine(fr);
		while(line != null){
			assert(line.get(1) != null);
			assert(line.get(2) != null);
			name = line.get(1);
			songFile = new File(line.get(2));
			
			songs.put(name, songFile);
			
			line = core.util.CSVParser.parseLine(fr);
		}
		fis.close();
		assert (songs.size() > 0);
	}
}
