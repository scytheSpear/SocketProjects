/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream.socket.client;

/**
 *
 * @author user
 */
import java.net.*;
import java.io.*;

public class StreamSocketClient {

   public static void main(String [] args) {
      String serverName = "172.16.16.236";
      int port = 23333;
      
      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         
         out.writeUTF("Hello from " + client.getLocalSocketAddress());
         out.writeUTF("test data abcde12345");
         
         
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Server says " + in.readUTF());
         client.close();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}
