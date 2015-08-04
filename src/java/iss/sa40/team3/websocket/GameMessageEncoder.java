
package iss.sa40.team3.websocket;

import iss.sa40.team3.model.Game;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
public class GameMessageEncoder implements Encoder.Text<Game> {
	@Override
	public void init(final EndpointConfig config) {
	}
 
	@Override
	public void destroy() {
	}
 
	@Override
	public String encode(final Game game) throws EncodeException {
		return game.toJson().toString();
	}
}
