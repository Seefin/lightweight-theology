package player;


/**A listener that is informed when player status changes.
 * 
 * Currently I only plan to have two signals - the song is over, and the song 
 * progress updates.
 * 
 * @author findlaconn
 *
 */
public interface PlayerListener {
	
	public void playerPerfomed(PlayerEvent e);

}
