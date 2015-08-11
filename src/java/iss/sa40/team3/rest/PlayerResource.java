package iss.sa40.team3.rest;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path ("/player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    
    //@EJB 
    private PlayerBean playerBean; 
    
    @Inject private Main main;
    
    @EJB
    public void setPlayerBean(PlayerBean p) {
        playerBean = p;
    }
    
    @GET
    @Path("{email}")
    public Response getPlayer(@PathParam("email") String email){
        Player player = new Player();
        if (email != null){
            player = playerBean.findPlayer(email);
        }
        if(player == null)
            return (Response.status(Response.Status.NOT_FOUND).build());
        return (Response.ok(player.toJson()).build());
    }
    
    @GET
    @Path("{email}/{name}/{password}/{highscore}")
    public Response updatePlayer(
            @PathParam("email") String email, 
            @PathParam("name")String name, 
            @PathParam("password") String password, 
            @PathParam("highscore") int highscore){
    
        boolean result = false;
        if(email != null && name != null && password != null){
            result = playerBean.updatePlayer(email, password, name, highscore);
        }
        if(result == false){
            return (Response.status(Response.Status.BAD_REQUEST).build());
        }
        return (Response.ok(Json.createObjectBuilder().build()).build());
    }
            
    @GET
    @Path("{email}/{name}/{password}")
    public Response createPlayer(
            @PathParam("email") String email, 
            @PathParam("name")String name, 
            @PathParam("password") String password){
        
        int highscore = 0;
        boolean result = false;
        if(email != null && name != null && password != null){
            result = playerBean.insertPlayer(email, password, name, highscore);
        }
        if(result == false){
            return (Response.status(Response.Status.BAD_REQUEST).build());
        }
        return (Response.ok(Json.createObjectBuilder().build()).build());
    }
    
    @GET
    @Path("{gameId}/{email}")
    public Response removePlayer(
            @PathParam("gameId") int gameId, 
            @PathParam("email")String email){
        
        //Get the game
        List<Game> games = main.getGames();
        Game selectedGame = null;
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        
        //Get Player
        Player player = null;
        if (email != null){
            player = playerBean.findPlayerFromGame(email, gameId);
        }
        if(player == null)
            return (Response.status(Response.Status.NOT_FOUND).build());
        
        //Compare player's new highscore with existing highscore
        HashMap<Player, Integer> playerscore = selectedGame.getPlayerscore();
        int currentHighscore = player.getHighscore();
        int newHighscore = playerscore.get(player);
        if(newHighscore > currentHighscore)
            player.setHighscore(newHighscore);
        
        //remove player from playerscore
        playerscore.remove(player);
        selectedGame.setPlayerscore(playerscore);
        
        return (Response.ok(selectedGame.toJson()).build());
        
    }
    
}
