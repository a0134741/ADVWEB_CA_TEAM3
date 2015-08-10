package iss.sa40.team3.business;

import iss.sa40.team3.model.Game;
import iss.sa40.team3.model.Main;
import iss.sa40.team3.rest.GameResource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    @Inject 
    private Main main;
    
    @Inject GameResource gameResource;
    
    public void startTimer(long durationMinute) {
        long durationMs = durationMinute * 60000;
        service.createTimer(durationMs, "null");
        System.out.println("Timers set");
    }
    
    @Timeout
    public void handleTimeout(Timer timer) throws ParseException {
        timer.cancel();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timeOut = df.format(new Date());
        List<Game> games = main.getGames();
        
        for(Game game : games){
            Date startTime = df.parse(game.getStartTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.MINUTE, Integer.parseInt(game.getDuration()));
            String endTime = df.format(cal.getTime());
            
            if(endTime == timeOut){
                JsonObject gameSummary = (JsonObject) gameResource.endGame(game.getGameId());
            } 
                
        }
        
        //call websocket to inform end game
        
        System.out.println("timeoutHandler : " + timer.getInfo());  
    }

//    public String checkTimerStatus() {
//        Timer timer = null;
//        Collection<Timer> timers = service.getTimers();
//        Iterator<Timer> iterator = timers.iterator();
//        while (iterator.hasNext()) {
//            timer = iterator.next();
//            return ("Timer will expire after " + 
//                    timer.getTimeRemaining() + " milliseconds.");
//        }
//        return ("No timer found");
//    }
}