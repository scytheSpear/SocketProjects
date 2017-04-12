/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.client;

import java.util.Timer;
import java.util.TimerTask;

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
            System.out.println(System.currentTimeMillis() + " Running");
            
            
            timer.schedule(new PeriodicTask(), 10 * 1000);
        }
    }
}