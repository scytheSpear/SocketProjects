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
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class SendFile {
    
    public SendFile(){
    
    }
    
    public void Send(ArrayList<File> files, Socket sk) throws IOException {

        Socket socket = sk;

        try {
//        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println(files.size());
//write the number of files to the server
            dos.writeInt(files.size());
            dos.flush();

            //write file names 
            for (int i = 0; i < files.size(); i++) {
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

                System.out.println(files.get(i).getName());
                //create new fileinputstream for each file
                FileInputStream fis = new FileInputStream(files.get(i));

                //write file to dos
                while ((n = fis.read(buf)) != -1) {
                    dos.write(buf, 0, n);
                    dos.flush();

                }
                files.get(i).renameTo(new File(files.get(i).getAbsolutePath()+ System.currentTimeMillis()+ ".read"));
                System.out.println("rename read file");
                
            }
            //or is this good?

            dos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
}
