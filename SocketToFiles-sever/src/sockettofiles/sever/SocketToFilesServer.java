package sockettofiles.sever;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SocketToFilesServer extends Thread {


    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket client = null;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("6666/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read properties file");
            System.exit(-1);
        }

       
        int port = Integer.valueOf(properties.getProperty("port"));
        System.out.println("read port value "+ port + " from properties file");
        
        
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not listen on port: "+ port);
            System.exit(-1);
        }

        System.out.println("Server is ready ");

        while (true) {
            try {
                client = serverSocket.accept();

                //get date as filename
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                //check file if exist, create new file if not
                File f = new File("/itsys-data/port"+ port + "_" + date + ".txt");
                f.createNewFile();

                Handle_Client_Request_Thread s = new Handle_Client_Request_Thread(client, f);
                s.start();
//                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
