package socket.forwarding;

import java.io.*;
import java.net.*;

public class SocketForwarding {

    public static final int SOURCE_PORT = 62000;
    public static final String DESTINATION_HOST = "172.16.16.236";
    public static final int DESTINATION_PORT = 62000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket
                = new ServerSocket(SOURCE_PORT);
        
        System.out.println("socket server started, forwarding port "+SOURCE_PORT+" to " + DESTINATION_HOST +":"+DESTINATION_PORT);
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientThread clientThread
                    = new ClientThread(clientSocket, DESTINATION_HOST, DESTINATION_PORT);
            clientThread.start();
        }
    }
}

//class ClientThread extends Thread {
//
//    private Socket mClientSocket;
//    private Socket mServerSocket;
//    private boolean mForwardingActive = false;
//    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//
//    public ClientThread(Socket aClientSocket) {
//        mClientSocket = aClientSocket;
//    }
//
//    public void run() {
//        InputStream clientIn;
////        OutputStream clientOut;
////        InputStream serverIn;
//        OutputStream serverOut;
//        try {
//            // Connect to the destination server 
//            mServerSocket = new Socket(
//                    SocketForwarding.DESTINATION_HOST,
//                    SocketForwarding.DESTINATION_PORT);
//
//            // Turn on keep-alive for both the sockets 
//            mServerSocket.setKeepAlive(true);
//            mClientSocket.setKeepAlive(true);
//
//            // Obtain client & server input & output streams 
//            clientIn = mClientSocket.getInputStream();
////            clientOut = mClientSocket.getOutputStream();
////            serverIn = mServerSocket.getInputStream();
//
//            serverOut = mServerSocket.getOutputStream();
//        } catch (IOException ioe) {
//            System.err.println("Can not connect to "
//                    + SocketForwarding.DESTINATION_HOST + ":"
//                    + SocketForwarding.DESTINATION_PORT);
//            connectionBroken();
//            return;
//        }
//
//        // Start forwarding data between server and client 
//        mForwardingActive = true;
//        int count=0;
//        try {
//            count = clientIn.available();
//        } catch (IOException ex) {
//            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        ForwardThread clientForward
//                = new ForwardThread(this, clientIn, serverOut);
//        clientForward.start();
//
//        Date date = new Date();
//        
//        System.out.println("read "+ count +" from client input at" + dateFormat.format(date)
//                + "\n forward to " + mServerSocket.getInetAddress() + ":" + mServerSocket.getPort());
//
////        ForwardThread serverForward
////                = new ForwardThread(this, serverIn, clientOut);
////        serverForward.start();
////        System.out.println("TCP Forwarding "
////                + mClientSocket.getInetAddress().getHostAddress()
////                + ":" + mClientSocket.getPort() + " <--> "
////                + mServerSocket.getInetAddress().getHostAddress()
////                + ":" + mServerSocket.getPort() + " started.");
//    }
//
//    public synchronized void connectionBroken() {
//        try {
//            mServerSocket.close();
//        } catch (Exception e) {
//        }
//        try {
//            mClientSocket.close();
//        } catch (Exception e) {
//        }
//
//        if (mForwardingActive) {
////            System.out.println("TCP Forwarding "
////                    + mClientSocket.getInetAddress().getHostAddress()
////                    + ":" + mClientSocket.getPort() + " <--> "
////                    + mServerSocket.getInetAddress().getHostAddress()
////                    + ":" + mServerSocket.getPort() + " stopped.");
//            mForwardingActive = false;
//        }
//    }
//}

//class ForwardThread extends Thread {
//
//    private static final int BUFFER_SIZE = 8192;
//
//    InputStream mInputStream;
//    OutputStream mOutputStream;
//    ClientThread mParent;
//
//    public ForwardThread(ClientThread aParent, InputStream aInputStream, OutputStream aOutputStream) {
//        mParent = aParent;
//        mInputStream = aInputStream;
//        mOutputStream = aOutputStream;
//    }
//
//    public void run() {
//        byte[] buffer = new byte[BUFFER_SIZE];
//        try {
//            while (true) {
//                int bytesRead = mInputStream.read(buffer);
//                if (bytesRead == -1) {
//                    break; // End of stream is reached --> exit 
//                }
//                mOutputStream.write(buffer, 0, bytesRead);
//                mOutputStream.flush();
//            }
//        } catch (IOException e) {
//            // Read/write failed --> connection is broken 
//        }
//
//        // Notify parent thread that the connection is broken 
//        mParent.connectionBroken();
//    }
//}
