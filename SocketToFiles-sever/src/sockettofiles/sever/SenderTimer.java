/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettofiles.sever;

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


    public void startTask() {
        timer.schedule(new PeriodicTask(), 0);
    }

    private class PeriodicTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println(System.currentTimeMillis() + " Running");
                
                SendFile s = new SendFile("172.16.16.235");
                s.read();
                
//                System.out.println(System.currentTimeMillis() + " Scheduling 10 seconds from now");
                timer.schedule(new PeriodicTask(), 10 * 1000);
            } catch (IOException ex) {
                Logger.getLogger(SenderTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}