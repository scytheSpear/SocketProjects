package server;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Forward_Client extends Thread {

    private String hostIp = null;
    private int remotePort = 0;
    private int localPort = 0;
    
    public Forward_Client(String host, int remoteport, int localport) {
        super("Forward_Client");
        hostIp = host;
        remotePort = remoteport;
        localPort= localport;
    }

    @Override
    public void run() {
        
        try {
            ServerSocket ss = new ServerSocket(localPort);
            
            final byte[] request = new byte[1024];
            byte[] reply = new byte[4096];
            
            while (true) {
                Socket client = null, server = null;
                try {
                    // Wait for a connection on the local port
                    client = ss.accept();
                    System.out.println("accept socket");
                    
                    final InputStream streamFromClient = client.getInputStream();
                    final OutputStream streamToClient = client.getOutputStream();
                    
                    // Make a connection to the real server.
                    // If we cannot connect to the server, send an error to the
                    // client, disconnect, and continue waiting for connections.
                    try {
                        server = new Socket(hostIp, remotePort);
                    } catch (IOException e) {
                        PrintWriter out = new PrintWriter(streamToClient);
                        out.print("Proxy server cannot connect to " + hostIp + ":"
                                + remotePort + ":\n" + e + "\n");
                        out.flush();
                        client.close();
                        continue;
                    }
                    
                    final OutputStream streamToServer = server.getOutputStream();
                    
                    // a thread to read the client's requests and pass them
                    // to the server. A separate thread for asynchronous.
                    Thread t = new Thread() {
                        public void run() {
                            int bytesRead;
                            System.out.println("send to remote");
                            try {
                                while ((bytesRead = streamFromClient.read(request)) != -1) {
                                    streamToServer.write(request, 0, bytesRead);
                                    streamToServer.flush();
                                }
                            } catch (IOException e) {
                            }

                            // the client closed the connection to us, so close our
                            // connection to the server.
                            try {
                                streamToServer.close();
                            } catch (IOException e) {
                            }
                        }
                    };
                    
                    // Start the client-to-server request thread running
                    t.start();
                    
                    streamToClient.close();
                } catch (IOException e) {
                    System.err.println(e);
                } finally {
                    try {
                        if (server != null) {
                            server.close();
                        }
                        if (client != null) {
                            client.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Forward_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
