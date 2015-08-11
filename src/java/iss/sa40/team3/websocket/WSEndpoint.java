
package iss.sa40.team3.websocket;

import iss.sa40.team3.game.GameWebSocket;
import iss.sa40.team3.model.ChatMessage;
import iss.sa40.team3.model.Game;
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
import java.util.Date;

@RequestScoped
@ServerEndpoint(value = "/wssocket/{gameId}",
                encoders = {GameMessageEncoder.class,ChatMessageEncoder.class},
                decoders = {MessageDecoder.class})
public class WSEndpoint  {
	private final Logger log = Logger.getLogger(getClass().getName());
        
        @Inject private GameWebSocket gr;
        
	@OnOpen
	public void open(final Session session, @PathParam("gameId") final String gameId)
                throws IOException, InterruptedException, EncodeException{
		log.info("session openend and bound to Game: " + gameId);
                session.getUserProperties().put("gameId", gameId);
                log.info("GameId: " + gameId);
                int gameid=Integer.parseInt(gameId);
                Game g=gr.getGame(gameid);
                session.getBasicRemote().sendObject(g);
                ChatMessage cm=new ChatMessage();
                cm.setType(1);
                cm.setChatmessage("Hello");
                cm.setSender("dong");
                Date dt=new Date();
                cm.setReceivedtime(dt);
                session.getBasicRemote().sendObject(cm);
        }
        @OnMessage
	public void onMessage(final Session session, final Message msg) throws IOException, EncodeException {
            String a=session.getUserProperties().get("gameId").toString();
            int gameId = Integer.parseInt(session.getUserProperties().get("gameId").toString());
            if (msg instanceof ChatMessage) {
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
					&& (gameId==Integer.parseInt(session.getUserProperties().get("gameId").toString()))) 
                                        {
					s.getBasicRemote().sendObject(msg);
				}
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
              } 
              else if (msg instanceof VerifyChosenSetMessage) {
                  VerifyChosenSetMessage gms=(VerifyChosenSetMessage)msg;
                  String rems= gr.verifyChosenSet(gms.getGameId(), 
                                                   gms.getPosition1(),                                                  
                                                   gms.getPosition2(),                                                   
                                                   gms.getPosition3(), 
                                                   gms.getEmail());
                  if(rems.equals("EndGame")){
                          for (Session s : session.getOpenSessions()) {
                                  if (s.isOpen()
                                        && (gameId==Integer.parseInt(session.getUserProperties().get("gameId").toString())))  {
                                          s.getBasicRemote().sendText(rems);
                                  }
                          }
                  }
                  else{
                       for (Session s : session.getOpenSessions()) {
                                  if (s.isOpen()
                                        && (gameId==Integer.parseInt(session.getUserProperties().get("gameId").toString()))) {
                                          s.getBasicRemote().sendText(rems);
                                  }
                          }
                      
                  }
              }
	} 
}