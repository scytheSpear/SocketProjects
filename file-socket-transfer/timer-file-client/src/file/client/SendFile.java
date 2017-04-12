/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 *
 * @author user
 */
public class SendFile {

    private Socket socket;
    private String hostName;
    private int port;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SendFile(String host, int port) throws IOException {
        this.hostName = host;
        this.port = port;
    }

    public void Send() throws IOException {

        try {
            ArrayList<File> files = new ArrayList<File>();

            Collection filesCollection = FileUtils.listFiles(
                    new File("/itsys-data"),
                    new RegexFileFilter("^.*?\\.(txt)$"),
                    DirectoryFileFilter.DIRECTORY
            );

            files.addAll(filesCollection);

//        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            Date date = new Date();
            System.out.println(files.size() + " files to be send at " + dateFormat.format(date));
//write the number of files to the server
//            dos.writeInt(files.size());
//            dos.flush();

            //write file names 
            for (int i = 0; i < files.size(); i++) {

                socket = new Socket(hostName, port);
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                dos.writeUTF(files.get(i).getAbsolutePath());
                dos.flush();
//        }
                dos.writeLong(files.get(i).length());
                dos.flush();
                //buffer for file writing, to declare inside or outside loop?
                int n = 0;
                byte[] buf = new byte[4092];
                //outer loop, executes one for each file
//        for(int i =0; i < files.size(); i++){

                System.out.println("send file: " + files.get(i).getName() + " , file lenth is " + files.get(i).length());
                //create new fileinputstream for each file
                FileInputStream fis = new FileInputStream(files.get(i));

                //write file to dos
                while ((n = fis.read(buf)) != -1) {
                    dos.write(buf, 0, n);
                    dos.flush();

                }
                files.get(i).renameTo(new File(files.get(i).getAbsolutePath() + System.currentTimeMillis() + ".read"));
                System.out.println("rename read file");

                dos.close();
                socket.close();
            }
//            dos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
