package iss.sa40.team3.test;

import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.rest.GameResource;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

public class GameResourceTest {
    
    @Test
    public void testCreateGame(){
        GameResource game = new GameResource();
       
        Main main = mock(Main.class);
        game.setMain(main);
        
        ArgumentCaptor<Game> gameCaptor = 
                ArgumentCaptor.forClass(Game.class);
        
        String title = "Game Test";
        String duration = "20";
        int maxPlayers = 10;
        
        List<Game> games = new ArrayList<Game>();
        when(main.getGames()).thenReturn(games);
        
        Response resp;
        try{
            resp = game.createGame(title, duration, maxPlayers);
        }catch(Exception e){
            fail();
            return;
        }
        
        verify(main, atLeast(1)).getGames();
        verify(main, atLeast(1)).setGames((List<Game>) gameCaptor.capture());
        
        assertThat((List<Game>)gameCaptor.getValue(),hasSize(greaterThan(0)));
        
        assertEquals(200, resp.getStatus());
        
    }
    
}
