package core;

import java.io.InputStream;

public interface Player {

	void connect(String ip, int port) throws Exception;

	void play(InputStream in) throws Exception;

}
