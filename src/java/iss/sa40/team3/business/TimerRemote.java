package iss.sa40.team3.business;

import javax.ejb.Remote;
 
@Remote
public interface TimerRemote {
    public String checkTimerStatus();
    public void startTimer(long durationMinute);
}
