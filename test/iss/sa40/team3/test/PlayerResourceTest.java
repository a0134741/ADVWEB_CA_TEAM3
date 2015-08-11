package iss.sa40.team3.test;

import iss.sa40.team3.business.PlayerBean;
import iss.sa40.team3.rest.PlayerResource;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PlayerResourceTest {
    
    @Test
    public void testCreatePlayer(){
        PlayerResource player = new PlayerResource();
        
        PlayerBean playerBean = mock(PlayerBean.class);
        player.setPlayerBean(playerBean);
        
        String email = "abc@gmail.com"; 
        String name = "abc"; 
        String password = "123456";
        int highscore = 0;
        
        when(playerBean.insertPlayer(email, password, name, highscore)).thenReturn(true);
        
        Response resp;
        try{
            resp = player.createPlayer(email, name, password);
        }catch(Exception e){
            fail();
            return;
        }
        
        verify(playerBean, atLeast(1)).insertPlayer(email, password, name, highscore);
        
        assertEquals(200, resp.getStatus());
        
    }
    
}
