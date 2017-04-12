/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author user
 */
public class FileClient {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        String host;
        int port;
        long period;
        long fileReserveTime;
        long fileRemoveFequency;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("timer-file-client-conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read properties file");
            System.exit(-1);
        }

        host = properties.getProperty("host");
        System.out.println("read host value " + host + " from properties file");
        port = Integer.valueOf(properties.getProperty("port"));
        System.out.println("read port value " + port + " from properties file");
        period = 1000 * Integer.valueOf(properties.getProperty("timerPeriodInSeconds"));
        System.out.println("read timerPeriod in seconds, value is " + period / 1000 + " from properties file");
        fileReserveTime = 60*60 * 1000 * Integer.valueOf(properties.getProperty("fileReserveTime"));
        System.out.println("read fileReserveTime in hours, value is " + fileReserveTime / 1000 / 60 / 60 + " from properties file");
        fileRemoveFequency = 24 * 60 * 60 * 1000 * Integer.valueOf(properties.getProperty("fileRemoveFequencyInDay"));
        System.out.println("read fileRemoveFequency in day, value is " + fileRemoveFequency / 1000 / 60 / 60 / 24 + " from properties file");

        RemoveOverdateFileTimer rTimer = new RemoveOverdateFileTimer(fileReserveTime, fileRemoveFequency);
        rTimer.startTask();

        System.out.println("file sender start in 5 -----");
        Thread.sleep(5000);

        SenderTimer timer = new SenderTimer(host, port, period);
        timer.startTask();

    }

}
