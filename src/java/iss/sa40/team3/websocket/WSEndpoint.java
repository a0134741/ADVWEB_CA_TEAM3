
package iss.sa40.team3.websocket;


import iss.sa40.team3.game.GameResource;
import iss.sa40.team3.game.MainResource;
import iss.sa40.team3.model.ChatMessage;
import iss.sa40.team3.model.CreategameMessage;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@RequestScoped
@ServerEndpoint(value = "/wssocket/{room}",
                encoders = {GameMessageEncoder.class,ChatMessageEncoder.class},
                decoders = {MessageDecoder.class})
public class WSEndpoint  {
	private final Logger log = Logger.getLogger(getClass().getName());
        
        @Inject private GameResource gr;
        @Inject private MainResource mr;
        
	@OnOpen
	public void open(Session session, @PathParam("room") final String room)
                throws IOException, InterruptedException, EncodeException{
		log.info("session openend and bound to room: " + room);
                session.getBasicRemote().sendText("session openend and bound to room: " + room);
                session.getBasicRemote().sendText(mr.getAllGames());
		
        }
        @OnMessage
	public void onMessage(final Session session, final Message msg) throws IOException, EncodeException {
              if (msg instanceof ChatMessage) {
                   CreategameMessage cgm=(CreategameMessage)msg;
               } else if (msg instanceof CreategameMessage) {
                   CreategameMessage cgm=(CreategameMessage)msg;
                   Game game=gr.createGame(cgm.getTitle(), cgm.getDuration(), cgm.getMaxPlayers());
                   try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()) {
					s.getBasicRemote().sendObject(game);
				}
			}
                    } catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
                    }
             }
	} 
        /*
	@OnMessage
	public void onMessage(final Session session, final ChatMessage chatMessage) {
		String room = (String) session.getUserProperties().get("room");
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
						&& room.equals(s.getUserProperties().get("room"))) {
					s.getBasicRemote().sendObject(chatMessage);
				}
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
	} 
        */
}
