package file.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class FileServer {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket client = null;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("conf.properties"));
        } catch (IOException e) {
            System.err.println("Could not read properties file");
            System.exit(-1);
        }

        int port = Integer.valueOf(properties.getProperty("port"));
        System.out.println("read port value " + port + " from properties file");

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(-1);
        }

        System.out.println("Server is ready ");

        while (true) {
            try {
                client = serverSocket.accept();

                FileServerThread s = new FileServerThread(client);
                s.start();
//                sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}
