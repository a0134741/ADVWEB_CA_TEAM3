package iss.sa40.team3.test;

import iss.sa40.team3.business.CardBean;
import iss.sa40.team3.model.Card;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.rest.GameResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

public class GameResourceTest {
    
    @Test
    public void testCreateGame(){
        GameResource game = new GameResource();
        
        CardBean cardBean = mock(CardBean.class);
        Main main = mock(Main.class);
        
        game.setCardBean(cardBean);
        game.setMain(main);
        
        ArgumentCaptor<Game> gameCaptor = 
                ArgumentCaptor.forClass(Game.class);
        
        String title = "Game Test";
        String duration = "20";
        int maxPlayers = 10;
        
        //mock deck and table
        List<Card> deck = new ArrayList<>(81);
        for(int number = 1; number< 4; number++){
            for(int shape = 1; shape < 4; shape++){
                for(int shading = 1; shading < 4; shading++){
                    for(int color = 1; color < 4; color++){
                        deck.add(new Card(number, shape, shading, color));
                    }
                }
            }
        }
        Collections.sort(deck);
        when(cardBean.getShuffledDeck()).thenReturn(deck);
        
        Card[]  table = new Card[12];
        List<Object> list = new ArrayList<>();
        for (int i=0; i < 12; i++) {
                Card card = deck.remove(deck.size() - 1); 
                table[i] = card;
        }
        
        list.add(deck);
        list.add(table);
        
        when(cardBean.issue12Cards(deck, table)).thenReturn(list);
//        when(list.get(0)).thenReturn(deck);
//        when(list.get(1)).thenReturn(table);
        when(cardBean.setExists(table)).thenReturn(true);
        
        List<Game> games = new ArrayList<Game>();
        when(main.getGames()).thenReturn(games);
        
        try{
            game.createGame(title, duration, maxPlayers);
        }catch(Exception e){
            e.printStackTrace();
            fail();
            return;
        }
        
        verify(cardBean, atLeast(1)).getShuffledDeck();
        verify(cardBean, atLeast(1)).issue12Cards(anyList(), any());
        verify(cardBean, atLeast(1)).setExists(any());
        verify(main, atLeast(1)).getGames();
        verify(main, atLeast(1)).setGames((List<Game>) gameCaptor.capture());
        
        assertThat((List<Game>)gameCaptor.getValue(),hasSize(greaterThan(0)));
        
        
        
    }
    
}
