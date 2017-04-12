package sockettofiles.sever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.File;
import java.io.FileWriter;

public class Handle_Client_Request_Thread extends Thread {

    private Socket socket = null;
    private BufferedReader rdr = null;
    private File file = null;

    public Handle_Client_Request_Thread(Socket s, File f) {
        this.socket = s;
        this.file = f;
    }

    public synchronized void writeToFile(String str) {
        try {
            System.out.println("create file writer for file " + file.getName());
            FileWriter fw = new FileWriter(file, true);
            fw.write(str);
            fw.write(System.getProperty("line.separator"));
            fw.flush();
            fw.close();
            System.out.println("writed to file");

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public void run() {
        try {

            rdr = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));

            while (true) {
                String line = rdr.readLine();
                if (line != null) {
                    System.out.println("log from Client socket thread " + Thread.currentThread().getName());
                    String a = line.trim();
                    System.out.println(a);
                    System.out.println("client :" + a);
                    System.out.println("add to file");
                    writeToFile(a);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                rdr.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
