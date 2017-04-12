/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream.socket.server;

/**
 *
 * @author user
 */
import java.net.*;
import java.io.*;

public class StreamSocketServer extends Thread {
   private ServerSocket serverSocket;
   
   public StreamSocketServer(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(0);
   }

   public void run() {
      while(true) {
         try {
            System.out.println("Waiting for client on port " + 
               serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            
            System.out.println(in.readUTF());
            System.out.println(in.readUTF());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Connected at " + server.getLocalSocketAddress()
               + "\nConnection END");
//            server.close();
            
         }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String [] args) {
//      int port = Integer.parseInt(args[0]);
      int port = 6666;
      
      try {
         Thread t = new StreamSocketServer(port);
         t.start();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}
