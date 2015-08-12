package iss.sa40.team3.business;

import iss.sa40.team3.rest.GameResource;
import iss.sa40.team3.websocket.WSEndpoint;
import java.io.IOException;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.json.JsonObject;

@Singleton
public class TimerSessionBean {
    @Resource
    TimerService service;
    
    @Inject GameResource gameResource;
    
    @Inject WSEndpoint ws;
    
    public void startTimer(long durationMinute, int gameId) {
        long durationMs = durationMinute * 60000;
        //long durationMs = durationMinute * 1000;
        service.createTimer(durationMs, gameId);
        System.out.println("Timers set");
    }
    
    @Timeout
    public void handleTimeout(Timer timer) throws IOException{
        
        int gameId = (int) timer.getInfo();
        JsonObject gameSummary = (JsonObject) gameResource.endGame(gameId);        
        
        //call websocket to inform end game
        
        ws.SendtoSameGameConnectedSessions(gameSummary.toString(), gameId);
        
        timer.cancel();
        
        System.out.println(gameId);  
    }

}