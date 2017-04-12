package socket.forwarding;

import java.io.*;
import java.net.*;

public class SocketForwarding {

    public static final int SOURCE_PORT = 51000;
    public static final String DESTINATION_HOST = "172.16.16.221";
    public static final int DESTINATION_PORT = 51000;

    public static void main(String[] args) throws IOException {
        Socket aServerSocket;
        ServerSocket serverSocket;

        while (true) {
            serverSocket = new ServerSocket(SOURCE_PORT);
            System.out.println("socket server started, forwarding port " + SOURCE_PORT);

            aServerSocket = new Socket(DESTINATION_HOST, DESTINATION_PORT);
            aServerSocket.setKeepAlive(true);
//            aServerSocket.setSoTimeout(60000);
            System.out.println("start forward client socket to " + DESTINATION_HOST + ":" + DESTINATION_PORT);

            synchronized (aServerSocket) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("recive incomming socket connection, start handlethread");
                    ClientThread clientThread
                            = new ClientThread(clientSocket, aServerSocket);
                    clientThread.start();

                }
            }
        }

    }
}
