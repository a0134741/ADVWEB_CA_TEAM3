package iss.sa40.team3.game;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;


@ApplicationScoped
public class MainResource {
    
    @EJB private PlayerBean playerBean;
    @Inject private Main main;
    

    public String getAllGames(){
        
        List<Game> games = main.getGames();
        
        JsonArrayBuilder gamesArray = Json.createArrayBuilder();
        for (Game game : games){
            gamesArray.add(game.toJson());
        }
        
        if(games == null)
            return "null";
        return (Json.createObjectBuilder()
                            .add("gamesArray", gamesArray)
                            .build().toString());
    }
    
    public String getTopPlayers(){
        List<Player> topPlayers = playerBean.getTop10Players();
        
        JsonArrayBuilder topPlayersArray = Json.createArrayBuilder();
        for (Player player : topPlayers){
            topPlayersArray.add(player.toJson());
        };
        
        if(topPlayers == null)
            return "NOT_FOUND";
        return Json.createObjectBuilder()
                            .add("topPlayersArray", topPlayersArray)
                            .build().toString();
        
    }
    
    
}
