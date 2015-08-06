package iss.sa40.team3.test;

import iss.sa40.team3.business.CardBean;
import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.rest.GameResource;
import java.util.Collection;
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
