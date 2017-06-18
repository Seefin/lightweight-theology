package core.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import core.Player;

public class StreamPlayer implements Player {
	
	@Override
	public synchronized void play(final InputStream in) throws Exception {
		assert(in != null);
		AudioInputStream ais = AudioSystem.getAudioInputStream(in);
		try (Clip clip = AudioSystem.getClip()){
			clip.open(ais);
			clip.start();
			Thread.sleep(100);
			clip.drain();
		}
		
	}

	@Override
	public void connect(String ip, int port) throws Exception {
		System.out.println("Connecting to " + ip +":"+port);
		try(Socket socket = new Socket(ip,port)){
			if(socket.isConnected()){
				InputStream in = new BufferedInputStream(socket.getInputStream());
				play(in);
			}
		}
		
	}
}
