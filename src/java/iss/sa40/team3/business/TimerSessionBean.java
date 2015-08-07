package iss.sa40.team3.business;

import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

@Stateless
public class TimerSessionBean implements TimerRemote{
    @Resource
    TimerService service;

    @Override
    public void startTimer(long durationMinute) {
        long durationMs = durationMinute * 60000;
        service.createTimer(durationMs, "null");
        System.out.println("Timers set");
    }
    
    @Timeout
    public void handleTimeout(Timer timer) {
        timer.cancel();
        System.out.println("timeoutHandler : " + timer.getInfo());  
    }

    @Override
    public String checkTimerStatus() {
        Timer timer = null;
        Collection<Timer> timers = service.getTimers();
        Iterator<Timer> iterator = timers.iterator();
        while (iterator.hasNext()) {
            timer = iterator.next();
            return ("Timer will expire after " + 
                    timer.getTimeRemaining() + " milliseconds.");
        }
        return ("No timer found");
    }
}