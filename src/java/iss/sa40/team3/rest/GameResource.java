package iss.sa40.team3.rest;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.business.TimerSessionBean;
import iss.sa40.team3.model.Card;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import iss.sa40.team3.utilities.CardUtilities;
import iss.sa40.team3.websocket.WSEndpoint;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    //@EJB 
    private TimerSessionBean timerBean; 
    
    //@EJB 
    private PlayerBean playerBean;
   
    //@Inject 
    private Main main;

    @EJB
    public void setTimerBean(TimerSessionBean t) {
        timerBean = t;
    }
    
    @EJB
    public void setPlayerBean(PlayerBean p) {
        playerBean = p;
    }

    @Inject
    private WSEndpoint ws;

    @Inject
    public void setMain(Main m) {
        main = m;
    }

    @GET
    @Path("{title}/{duration}/{maxPlayers}")
    public Response createGame(
            @PathParam("title") String title,
            @PathParam("duration") String duration,
            @PathParam("maxPlayers") int maxPlayers) throws IOException {

        Card[] table = new Card[12];
        List<Card> deck = CardUtilities.getShuffledDeck();
        List<Object> list = CardUtilities.issue12Cards(deck, table);
        deck = (List<Card>) list.get(0);
        table = (Card[]) list.get(1);
        while (!CardUtilities.setExists(table)) {
            deck.clear();
            Arrays.fill(table, null);
            list.clear();
            deck = CardUtilities.getShuffledDeck();
            list = CardUtilities.issue12Cards(deck, table);
            deck = (List<Card>) list.get(0);
            table = (Card[]) list.get(1);
        }

        //System.out.println(CardUtilities.getAllSets(table, true));
        System.out.println(System.nanoTime() / (1000000000 * 60));
        //System.out.println(System.nanoTime()/(1000000000*60));

        Game game = new Game();
        if (title != null && duration != null && maxPlayers > 0) {
            game.setTitle(title);
            game.setDuration(duration);
            game.setDeck(deck);
            game.setTable(table);
            game.setMaxPlayers(maxPlayers);
            game.setRound(1);
        }

        List<Game> games = main.getGames();
        games.add(game);
        main.setGames(games);

        timerBean.startTimer(Long.parseLong(duration), game.getGameId());
        
        return (Response.ok(game.toJson()).build());
    }

    @GET
    @Path("{gameId}")
    public Response getGame(@PathParam("gameId") int gameId) {

        List<Game> games = main.getGames();
        Game selectedGame = null;
        for (Game game : games) {
            if (game.getGameId() == gameId) {
                selectedGame = game;
            }
        }
        if (selectedGame == null) {
            return (Response.status(Response.Status.NOT_FOUND).build());
        }
        return (Response.ok(selectedGame.toJson()).build());
    }

    //Threading on this method
    @Resource(mappedName = "concurrent/myThreadPool")
    private ManagedExecutorService executors;

    @GET
    @Path("{gameId}/{email}")
    public void joinGame(@Context HttpServletRequest req,
            @PathParam("gameId") int gameId,
            @PathParam("email") String email,
            @Suspended AsyncResponse async) {

        JoinGameTask joinGame = new JoinGameTask();
        joinGame.setJoinCriteria(main, playerBean, gameId, email);
        joinGame.setAsyncResponse(async);

        executors.execute(joinGame);
    }

    @GET
    @Path("endGame/{gameId}")
    public JsonObject endGame(@PathParam("gameId") int gameId) {

        //Get the game
        List<Game> games = main.getGames();
        Game selectedGame = null;
        for (Game game : games) {
            if (game.getGameId() == gameId) {
                selectedGame = game;
            }
        }

        //get game info
        String title = selectedGame.getTitle();
        int rounds = selectedGame.getRound();
        long start = selectedGame.getStart();
        long timeElapsedMinites = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start);
        long timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start);
        String mins = Long.toString(timeElapsedMinites);
        String sec = Long.toString(timeElapsedSeconds);
        String timeElapsed = mins + ":" + sec ;
        
        //Compare player's new highscore with existing highscore
        HashMap<Player, Integer> playerscore = selectedGame.getPlayerscore();
        if (playerscore != null) {
            Set playerSet = playerscore.keySet();
            Iterator playerIterator = playerSet.iterator();
            while (playerIterator.hasNext()) {
                Player player = (Player) playerIterator.next();
                int currentHighscore = player.getHighscore();
                int newHighscore = playerscore.get(player);
                if (newHighscore > currentHighscore) {
                    player.setHighscore(newHighscore);
                }
            }
        }

        games.remove(selectedGame);

        JsonArrayBuilder playerScoreArray = Json.createArrayBuilder();
        if (playerscore != null) {
            Set playerSet = playerscore.keySet();
            Iterator playerIterator = playerSet.iterator();
            while (playerIterator.hasNext()) {
                Player player = (Player) playerIterator.next();
                playerScoreArray.add(Json.createObjectBuilder()
                        .add("player", player.toJson())
                        .add("currentScore", playerscore.get(player)));
            }
        }
        
        return(/*Response.ok(*/Json.createObjectBuilder()
                            .add("type", 3)
                            .add("title", title)
                            .add("rounds", rounds)
                            .add("timeElapsed", timeElapsed)
                            .add("playerScoreArray", playerScoreArray)
                            .build())/*.build())*/;
    }

}
