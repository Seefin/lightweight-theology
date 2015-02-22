package player;

import java.util.EventObject;

public class PlayerEvent extends EventObject {
	private static final long serialVersionUID = -2477413425775169651L;
	private PlayerEvent _event;
	private String mess;
	private int id;
	
	public PlayerEvent(Object source, PlayerEvent e){
		super(source);
		_event = e;
	}
	
	public PlayerEvent(String m){
		super(null);
		mess = m;
		id = -1;
	}
	
	public PlayerEvent(String m, int idx){
		super(null);
		mess = m;
		this.id = idx;
	}
	
	public String toString(){
		return mess;
	}
	
	public PlayerEvent event(){
		return _event;
	}
	
	public String message(){
		return mess;
	}
	
	public int id(){
		return id;
	}
}