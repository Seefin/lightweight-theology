import java.io.File;

import core.Player;
import core.Streamer;
import core.impl.InetStreamer;
import core.impl.StreamPlayer;

/** Basic streaming application for (ATM) Audio.
 * Designed because I don't want to put up a share :)
 */

/**
 * @author cf704
 *
 */
public class Main {
	private static Streamer streamer;
	private static Player player;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		assert(args.length > 0);
		switch (args[0]){
		case "server":
			assert(args.length == 3);
			streamer = new InetStreamer(new File(args[1]));
			streamer.serveMusic(Integer.parseInt(args[2]));
			break;
		case "client":
			assert(args.length == 3);
			player = new StreamPlayer();
			player.connect(args[1], Integer.parseInt(args[2]));
			break;
		default:
			System.out.println("Args should be <server filename port> or <client ip port>");

		}
	}

}
