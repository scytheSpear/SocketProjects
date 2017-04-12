/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.forwarding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ClientThread extends Thread {

    private Socket mClientSocket;
    private Socket mServerSocket;
    private boolean mForwardingActive = false;
    
    private String dHost;
    private int dPort;
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public ClientThread(Socket aClientSocket, String aHost, int aPort) {
        mClientSocket = aClientSocket;
        dHost = aHost;
        dPort = aPort;
        
    }

    @Override
    public void run() {
        InputStream clientIn;
//        OutputStream clientOut;
//        InputStream serverIn;
        OutputStream serverOut;
        try {
            // Connect to the destination server 
            mServerSocket = new Socket(dHost,dPort);

            // Turn on keep-alive for both the sockets 
            mServerSocket.setKeepAlive(true);
            mClientSocket.setKeepAlive(true);

            // Obtain client & server input & output streams 
            clientIn = mClientSocket.getInputStream();
//            clientOut = mClientSocket.getOutputStream();
//            serverIn = mServerSocket.getInputStream();

            serverOut = mServerSocket.getOutputStream();
        } catch (IOException ioe) {
            System.err.println("Can not connect to "
                    + dHost+ ":"
                    + dPort);
            connectionBroken();
            return;
        }

        // Start forwarding data between server and client 
        mForwardingActive = true;
        int count = 0;
        try {
            count = clientIn.available();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        ForwardThread clientForward
                = new ForwardThread(this, clientIn, serverOut);
        clientForward.start();

        Date date = new Date();

        System.out.println("read " + count + " from client" + mClientSocket.getInetAddress().getHostAddress()
                + ":" + mClientSocket.getPort()+ " input at " + dateFormat.format(date)
                + "\n forward to " + mServerSocket.getInetAddress() + ":" + mServerSocket.getPort());

//      Forward socket back  

//        ForwardThread serverForward
//                = new ForwardThread(this, serverIn, clientOut);
//        serverForward.start();
//        System.out.println("TCP Forwarding "
//                + mClientSocket.getInetAddress().getHostAddress()
//                + ":" + mClientSocket.getPort() + " <--> "
//                + mServerSocket.getInetAddress().getHostAddress()
//                + ":" + mServerSocket.getPort() + " started.");
    }

    public synchronized void connectionBroken() {
        try {
            mServerSocket.close();
        } catch (Exception e) {
        }
        try {
            mClientSocket.close();
        } catch (Exception e) {
        }

        if (mForwardingActive) {
//            System.out.println("TCP Forwarding "
//                    + mClientSocket.getInetAddress().getHostAddress()
//                    + ":" + mClientSocket.getPort() + " <--> "
//                    + mServerSocket.getInetAddress().getHostAddress()
//                    + ":" + mServerSocket.getPort() + " stopped.");
            mForwardingActive = false;
        }
    }
}
