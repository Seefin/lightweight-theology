package player;

import java.awt.Event;
/**
 * The playerModel class uses these events to communicate with it's listeners.
 * 
 * @author findlaconn
 *
 */
public class PlayerEvent extends Event{
	
	private static final long serialVersionUID = 1127200673285192932L;
	public enum TYPES {STOPEVENT};
	public final String Message;
	public int songIdx;
	
	public PlayerEvent(Object target, int id, Object arg) {
		super(target, id, arg);
		Message = "Wrong Constructor!";
	}
	
	public PlayerEvent(Object target, int id, Object arg, String message){
		super(target,id,arg);
		Message = message;
		songIdx = -1;
	}
	
	public PlayerEvent(Object target, int id, Object arg, String message, int songIdx){
		super(target,id,arg);
		Message = message;
		this.songIdx = songIdx;
	}
}
