/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.client;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class RemoveOverdateFileTimer {

    private Timer timer = new Timer();
    private long fileReserveTime;
    private long fileRemoveFequency;

    public RemoveOverdateFileTimer(long fileReserveTime, long fileRemoveFequency) {
        this.fileReserveTime = fileReserveTime;
        this.fileRemoveFequency = fileRemoveFequency;
    }
    

    public void startTask() {
        timer.schedule(new PeriodicTask(), 0);
    }

    private class PeriodicTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("removing over date file, thread: " + Thread.currentThread().getName() + " Running");
                
                RemoveOverdateFile s = new RemoveOverdateFile(fileReserveTime);
                s.Delete();
                
                timer.schedule(new PeriodicTask(), fileRemoveFequency);
            } catch (IOException ex) {
                Logger.getLogger(RemoveOverdateFileTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}