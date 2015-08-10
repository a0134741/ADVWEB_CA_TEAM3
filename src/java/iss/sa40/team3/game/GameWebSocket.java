package iss.sa40.team3.game;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.model.Card;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.model.Player;
import iss.sa40.team3.utilities.CardUtilities;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GameWebSocket {
    
    @EJB private PlayerBean playerBean; 
    @Inject private Main main;
    
    public String verifyChosenSet(int gameId,
                                  int position1,
                                  int position2,
                                  int position3,
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
        
        boolean isSet = CardUtilities.setExists(set);
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
        selectedGame.setTable(CardUtilities.removeCards(position, selectedGame.getTable()));
        selectedGame.setRound(selectedGame.getRound()+1);
        
        
        //if there are cards in deck, return response 'ok' and id of 3 cards
        List<Object> list = CardUtilities.issue3Cards(position,selectedGame.getDeck(), selectedGame.getTable());
        selectedGame.setDeck((List<Card>) list.get(0));
        selectedGame.setTable((Card[]) list.get(1));
        
        if(!CardUtilities.setExists(selectedGame.getTable())){
            return "EndGame";
        }
        System.out.println(CardUtilities.getAllSets(table, true));
        return selectedGame.toJson().toString();
    } 
}
