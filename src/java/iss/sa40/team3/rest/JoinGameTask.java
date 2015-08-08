package iss.sa40.team3.rest;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import java.util.HashMap;
import java.util.List;
import javax.json.Json;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

public class JoinGameTask implements Runnable{
    
    private Main main;
    private PlayerBean playerBean; 
    private int gameId;
    private String email;
    private AsyncResponse async;
        
    public void setJoinCriteria(Main main, PlayerBean playerBean, int gameId, String email){
        this.main = main;
        this.playerBean = playerBean;
        this.gameId = gameId;
        this.email = email;
    }
    
    public void setAsyncResponse(AsyncResponse async){
        this.async = async;
    }
    
    @Override 
    public void run(){
        
        //Get game
        List<Game> games = main.getGames();
        Game selectedGame=null;
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        if(selectedGame == null)
            async.resume(Response.status(Response.Status.NOT_FOUND).build());
        
        //Get player
        Player player = new Player();
        
        if (email != null){
            player = playerBean.findPlayer(email);
        }
        if(player == null)
            async.resume(Response.status(Response.Status.NOT_FOUND).build());
        
        //Add player to game
        HashMap<Player, Integer> playerscore = selectedGame.getPlayerscore();
        if(playerscore == null)
            playerscore = new HashMap<>();
        playerscore.put(player, 0);
        selectedGame.setPlayerscore(playerscore);
        
        async.resume(Response.ok(Json.createObjectBuilder()
                            .add("gameId", selectedGame.getGameId())
                            .build()).build());
        
    }
    
}
