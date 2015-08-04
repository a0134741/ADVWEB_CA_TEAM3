package iss.sa40.team3.game;

import iss.sa40.team3.business.CardBean;
import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Card;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GameWebSocket {
    
    @EJB private CardBean cardBean;
    @EJB private PlayerBean playerBean; 
    @Inject private Main main;
    
    public Game createGame(String title,String duration,int maxPlayers){
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
        return game;
    }
    
    public Game getGame(int gameId){
        
        List<Game> games = main.getGames();
        Game selectedGame = null;
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        if(selectedGame == null)
            return selectedGame;
        return selectedGame;
    }
    

    public String verifyChosenSet(int gameId,
                                  int position1,
                                  int cardId1,
                                  int position2,
                                  int cardId2,
                                  int position3,
                                  int cardId3,
                                  String email){
        
        //Get the game
        List<Game> games = main.getGames();
        Game selectedGame = null;
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        if(selectedGame == null)
            return "NOT_FOUND";
        
        //Check if the three cards make a set
        Card[] table = selectedGame.getTable();
        Card card1 = table[position1];
        Card card2 = table[position2];
        Card card3 = table[position3];
        
        Card[] set = new Card[3];
        set[0] = card1;
        set[1] = card2;
        set[2] = card3;
        
        boolean isSet = cardBean.setExists(set);
        if(isSet == false)
            return "Not_Set";
        
        //Get Player
        Player player = null;
        if (email != null){
            player = playerBean.findPlayerFromGame(email, gameId);
        }
        if(player == null)
            return "NOT_FOUND";
        
        //Add +1 to player's score
        HashMap<Player, Integer> playerscore = selectedGame.getPlayerscore();
        int score = playerscore.get(player)+1;
        playerscore.put(player, score );
        
        int[] position = new int[3];
        position[0] = position1;
        position[1] = position2;
        position[2] = position3;
        
        //remove the set (3 cards) from table and round++
        selectedGame.setTable(cardBean.removeCards(position, selectedGame.getTable()));
        selectedGame.setRound(selectedGame.getRound()+1);
        
        
        //if there are cards in deck, return response 'ok' and id of 3 cards
        List<Object> list = cardBean.issue3Cards(position,selectedGame.getDeck(), selectedGame.getTable());
        selectedGame.setDeck((List<Card>) list.get(0));
        selectedGame.setTable((Card[]) list.get(1));
        
        if(!cardBean.setExists(selectedGame.getTable())){
            return "EndGame";
        }
        
        return selectedGame.toJson().toString();
    }
    

    public String joinGame(int gameId,String email){
        
        //Get game
        List<Game> games = main.getGames();
        Game selectedGame=null;
        for(Game game : games){
            if(game.getGameId() == gameId){
                selectedGame = game;
            }
        }
        if(selectedGame == null)
            return "NOT_FOUND";
        
        //Get player
        Player player = new Player();
        if (email != null){
            player = playerBean.findPlayer(email);
        }
        if(player == null)
            return "NOT_FOUND";
        
        //Add player to game
        HashMap<Player, Integer> playerscore = selectedGame.getPlayerscore();
        if(playerscore == null)
            playerscore = new HashMap<>();
        playerscore.put(player, 0);
        selectedGame.setPlayerscore(playerscore);
        
        return selectedGame.toJson().toString();
    }
    
}
