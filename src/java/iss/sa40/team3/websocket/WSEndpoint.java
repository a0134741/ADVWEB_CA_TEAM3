
package iss.sa40.team3.websocket;


import iss.sa40.team3.game.GameResource;
import iss.sa40.team3.game.MainResource;
import iss.sa40.team3.model.ChatMessage;
import iss.sa40.team3.model.CreategameMessage;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.GetgameMessage;
import iss.sa40.team3.model.JoinGameMessage;
import iss.sa40.team3.model.Message;
import iss.sa40.team3.model.VerifyChosenSetMessage;
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
@ServerEndpoint(value = "/wssocket/{gameId}",
                encoders = {GameMessageEncoder.class,ChatMessageEncoder.class},
                decoders = {MessageDecoder.class})
public class WSEndpoint  {
	private final Logger log = Logger.getLogger(getClass().getName());
        
        @Inject private GameResource gr;
        @Inject private MainResource mr;
        
	@OnOpen
	public void open(Session session, @PathParam("gameId") final String room)
                throws IOException, InterruptedException, EncodeException{
		log.info("session openend and bound to room: " + room);
                session.getBasicRemote().sendText("session openend and bound to room: " + room);
                session.getBasicRemote().sendText(mr.getAllGames());
		
        }
        @OnMessage
	public void onMessage(final Session session, final Message msg) throws IOException, EncodeException {
            String gameId = (String) session.getUserProperties().get("gameId");
            if (msg instanceof ChatMessage) {
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
					&& gameId.equals(s.getUserProperties().get("gameId"))) {
					s.getBasicRemote().sendObject(msg);
				}
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
              } 
              else if (msg instanceof CreategameMessage) {
                   CreategameMessage cgm=(CreategameMessage)msg;
                   Game game=gr.createGame(cgm.getTitle(), cgm.getDuration(), cgm.getMaxPlayers());
                   session.getBasicRemote().sendObject(game);
              }
              else if (msg instanceof GetgameMessage) {
                  GetgameMessage gms=(GetgameMessage)msg;
                  session.getBasicRemote().sendObject(gr.getGame(gms.getGameId()));
              }
              else if (msg instanceof VerifyChosenSetMessage) {
                  VerifyChosenSetMessage gms=(VerifyChosenSetMessage)msg;
                  String rems=  gr.verifyChosenSet(gms.getGameId(), 
                                                   gms.getPosition1(), 
                                                   gms.getCardId1(), 
                                                   gms.getPosition2(), 
                                                   gms.getCardId2(), 
                                                   gms.getPosition3(), 
                                                   gms.getCardId3(), 
                                                   gms.getEmail());
                  if(rems.equals("EndGame")){
                          for (Session s : session.getOpenSessions()) {
                                  if (s.isOpen()
                                        && gameId.equals(s.getUserProperties().get("gameId"))) {
                                          s.getBasicRemote().sendText(rems);
                                  }
                          }
                  }
                  else{
                      session.getBasicRemote().sendText(rems);
                  }
              }
              else if (msg instanceof JoinGameMessage) {
                  JoinGameMessage gms=(JoinGameMessage)msg;
                  
              }
	} 
}
