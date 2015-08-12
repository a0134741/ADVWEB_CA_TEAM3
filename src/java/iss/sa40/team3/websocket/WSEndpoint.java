package iss.sa40.team3.websocket;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.game.GameWebSocket;
import iss.sa40.team3.model.ChatMessage;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Message;
import iss.sa40.team3.model.Player;
import iss.sa40.team3.model.VerifyChosenSetMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;

@ApplicationScoped
@ServerEndpoint(value = "/wssocket/{gameId}",
        encoders = {GameMessageEncoder.class, ChatMessageEncoder.class},
        decoders = {MessageDecoder.class})
public class WSEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static HashSet<Session> sessions = new HashSet<Session>();
    private Player player;

    @Inject
    private GameWebSocket gr;

    @EJB
    private PlayerBean playerBean;

    @OnOpen
    public void open(final Session session, @PathParam("gameId") final String gameId)
            throws IOException, InterruptedException, EncodeException {

        log.info("session openend and bound to Game: " + gameId);
        session.getUserProperties().put("gameId", gameId);
        log.info("GameId: " + gameId);
        int gameid = Integer.parseInt(gameId);
        Game g = gr.getGame(gameid);
        log.info("Game: " + g.toString());
        sessions.add(session);
        
        if(g.getPlayerscore()!=null){
            List<Player> keys = new ArrayList(g.getPlayerscore().keySet());
        player = keys.get(g.getPlayerscore().size() - 1);
        SendSystemMessage(player.getName() + " has joined!", gameid);
        }
        SendGametoSameGameSessions(gr.getGame(gameid),gameid);
        
    }

    @OnMessage
    public void onMessage(final Session session, final Message msg) throws IOException, EncodeException {
        int gameId = Integer.parseInt(session.getUserProperties().get("gameId").toString());
        //If the message is chat
        if (msg instanceof ChatMessage) {
            ChatMessage c = (ChatMessage) msg;
            SendChattoSameGameSessions(c, gameId);
            //if the message is game 
        } else if (msg instanceof VerifyChosenSetMessage) {
            VerifyChosenSetMessage gms = (VerifyChosenSetMessage) msg;
            //Go to verify the cards
            String rems = gr.verifyChosenSet(gms.getGameId(),
                    gms.getPosition1(),
                    gms.getPosition2(),
                    gms.getPosition3(),
                    gms.getEmail());
            if (rems.equals("Not_Set") || rems.equals("NOT_FOUND")) {
                //send the result to self
                session.getBasicRemote().sendText(rems);
            } else {
                //send the result to all
                SendtoSameGameConnectedSessions(rems, gameId);
                SendSystemMessage(player.getName() + " has found a set!", gameId);
            }
        }
    }

    @OnClose
    public void onClose(final Session session) throws IOException, EncodeException {
        int gameId = Integer.parseInt(session.getUserProperties().get("gameId").toString());
        if (player != null) {
            SendSystemMessage(player.getName() + " has left game!", gameId);
        }
    }

    public void SendSystemMessage(String message, int gameId) throws IOException, EncodeException {
        ChatMessage c = new ChatMessage();
        c.setSender("System");
        c.setChatmessage(message);
        Date dt = new Date();
        c.setReceivedtime(dt);
        SendChattoSameGameSessions(c, gameId);
    }

    public void SendtoSameGameConnectedSessions(String message, int gameId) throws IOException {
        log.info(">>>" + sessions.toString());
        log.info(">>>" + message);
        for (Session s : sessions) {
            if (s.isOpen()
                    && (gameId == Integer.parseInt(s.getUserProperties().get("gameId").toString()))) {
                s.getBasicRemote().sendText(message);
            }
        }
    }

    public void SendGametoSameGameSessions(Game g, int gameId) throws IOException, EncodeException {
        log.info(">>>" + sessions.toString());
        for (Session s : sessions) {
            if (s.isOpen()
                    && (gameId == Integer.parseInt(s.getUserProperties().get("gameId").toString()))) {
                s.getBasicRemote().sendObject(g);
            }
        }
    }

    public void SendChattoSameGameSessions(ChatMessage c, int gameId) throws IOException, EncodeException {
        log.info(">>>" + sessions.toString());
        for (Session s : sessions) {
            if (s.isOpen()
                    && (gameId == Integer.parseInt(s.getUserProperties().get("gameId").toString()))) {
                s.getBasicRemote().sendObject(c);
            }
        }
    }

    public void SendtoAll(String message) throws IOException {
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(message);
            }
        }
    }

}
