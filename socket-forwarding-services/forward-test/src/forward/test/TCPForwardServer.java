package forward.test;

import java.io.*;
import java.net.*;

public class TCPForwardServer {

    public static final int SOURCE_PORT = 3333;
    public static final String DESTINATION_HOST = "172.16.16.235";
    public static final int DESTINATION_PORT = 3333;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket
                = new ServerSocket(SOURCE_PORT);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientThread clientThread
                    = new ClientThread(clientSocket);
            clientThread.start();
        }
    }
}

/**
 * ClientThread is responsible for starting forwarding between the client and
 * the server. It keeps track of the client and servers sockets that are both
 * closed on input/output error durinf the forwarding. The forwarding is
 * bidirectional and is performed by two ForwardThread instances.
 */
class ClientThread extends Thread {

    private Socket mClientSocket;
    private Socket mServerSocket;
    private boolean mForwardingActive = false;

    public ClientThread(Socket aClientSocket) {
        mClientSocket = aClientSocket;
    }

    public void run() {
        InputStream clientIn;
//        OutputStream clientOut;
//        InputStream serverIn;
        OutputStream serverOut;
        try {
            // Connect to the destination server 
            mServerSocket = new Socket(
                    TCPForwardServer.DESTINATION_HOST,
                    TCPForwardServer.DESTINATION_PORT);

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
                    + TCPForwardServer.DESTINATION_HOST + ":"
                    + TCPForwardServer.DESTINATION_PORT);
            connectionBroken();
            return;
        }

        // Start forwarding data between server and client 
        mForwardingActive = true;
        ForwardThread clientForward
                = new ForwardThread(this, clientIn, serverOut);
        clientForward.start();
        
//        ForwardThread serverForward
//                = new ForwardThread(this, serverIn, clientOut);
//        serverForward.start();

        System.out.println("TCP Forwarding "
                + mClientSocket.getInetAddress().getHostAddress()
                + ":" + mClientSocket.getPort() + " <--> "
                + mServerSocket.getInetAddress().getHostAddress()
                + ":" + mServerSocket.getPort() + " started.");
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
            System.out.println("TCP Forwarding "
                    + mClientSocket.getInetAddress().getHostAddress()
                    + ":" + mClientSocket.getPort() + " <--> "
                    + mServerSocket.getInetAddress().getHostAddress()
                    + ":" + mServerSocket.getPort() + " stopped.");
            mForwardingActive = false;
        }
    }
}

/**
 * ForwardThread handles the TCP forwarding between a socket input stream
 * (source) and a socket output stream (dest). It reads the input stream and
 * forwards everything to the output stream. If some of the streams fails, the
 * forwarding stops and the parent is notified to close all its sockets.
 */
class ForwardThread extends Thread {

    private static final int BUFFER_SIZE = 8192;

    InputStream mInputStream;
    OutputStream mOutputStream;
    ClientThread mParent;


    public ForwardThread(ClientThread aParent, InputStream aInputStream, OutputStream aOutputStream) {
        mParent = aParent;
        mInputStream = aInputStream;
        mOutputStream = aOutputStream;
    }

    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            while (true) {
                int bytesRead = mInputStream.read(buffer);
                if (bytesRead == -1) {
                    break; // End of stream is reached --> exit 
                }
                mOutputStream.write(buffer, 0, bytesRead);
                mOutputStream.flush();
            }
        } catch (IOException e) {
            // Read/write failed --> connection is broken 
        }

        // Notify parent thread that the connection is broken 
        mParent.connectionBroken();
    }
}
