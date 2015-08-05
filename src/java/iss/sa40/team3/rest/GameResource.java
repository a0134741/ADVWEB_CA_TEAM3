package iss.sa40.team3.rest;

import iss.sa40.team3.business.CardBean;
import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Card;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import java.util.ArrayList;
import java.util.Arrays;
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
@Path ("/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
    
    @EJB private CardBean cardBean;
    @EJB private PlayerBean playerBean; 
    @Inject private Main main;
    
    @GET
    @Path("{title}/{duration}/{maxPlayers}")
    public Response createGame(
            @PathParam("title") String title, 
            @PathParam("duration")String duration, 
            @PathParam("maxPlayers") int maxPlayers){
        
        Card[]  table = new Card[12];
        List<Card> deck = cardBean.getShuffledDeck();
        List<Object> list = cardBean.issue12Cards(deck, table);
        deck = (List<Card>) list.get(0);
        table = (Card[]) list.get(1);
        while(!cardBean.setExists(table)){
            deck.clear();
            Arrays.fill(table, null);
            list.clear();
            deck = cardBean.getShuffledDeck();
            list = cardBean.issue12Cards(deck, table);
            deck = (List<Card>) list.get(0);
            table = (Card[]) list.get(1);
        }
        
        System.out.println(cardBean.getAllSets(table, true));
        
        Game game = new Game();
        if (title != null && duration != null && maxPlayers>0){
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
        
        return (Response.ok(game.toJson()).build());
    }
    
    @GET
    @Path("{gameId}")
    public Response getGame(@PathParam("gameId") int gameId){
        
        List<Game> games = main.getGames();
        Game selectedGame = new Game();
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        if(selectedGame == null)
            return (Response.status(Response.Status.NOT_FOUND).build());
        return (Response.ok(selectedGame.toJson()).build());
    }
    
}