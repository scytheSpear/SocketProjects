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
public class SenderTimer {

    private Timer timer = new Timer();
    private String hostname;
    private int port;
    private long timePeriod;

    public SenderTimer(String hostname, int port, long timePeriod) {
        this.hostname = hostname;
        this.port = port;
        this.timePeriod = timePeriod;
    }
    

    public void startTask() {
        timer.schedule(new PeriodicTask(), 0);
    }

    private class PeriodicTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " Running");
                
                SendFile s = new SendFile(hostname, port);
                s.Send();
                
                timer.schedule(new PeriodicTask(), timePeriod);
            } catch (IOException ex) {
                Logger.getLogger(SenderTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}