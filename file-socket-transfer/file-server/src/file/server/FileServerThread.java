package file.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileServerThread extends Thread {

    Socket socket = null;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public FileServerThread(Socket sk) {
        this.socket = sk;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//          read the number of files from the client
//            int number = dis.readInt();
//            int n = 0;
//            byte[] buf = new byte[4092];
//            
//            ArrayList<File> files = new ArrayList<File>(number);
            Date date = new Date();
//            System.out.println("Number of Files to be received: " + number + " at " + dateFormat.format(date));

            System.out.println("Receive file at " + dateFormat.format(date));
            //read file names, add files to arraylist
//            for (int i = 0; i < number; i++) {
//                File file = new File(dis.readUTF());
//                files.add(file);
//            }
                int n = 0;
                byte[] buf = new byte[4092];

                String filename = dis.readUTF();
                long fileSize = dis.readLong();
                 System.out.println("file path is : " + filename + " lenth is " + fileSize);
                try (FileOutputStream fos = new FileOutputStream(filename + "-" + Thread.currentThread().getId() + "-" + String.valueOf(System.currentTimeMillis()).substring(4) + ".txt")) {

                    while (fileSize > 0 && (n = dis.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
                        fos.write(buf, 0, n);
                        fileSize -= n;
                    }
                    
//                    File f = new File    
//                            renameTo(new File(files.get(i).getAbsolutePath() + System.currentTimeMillis() + ".read"));
//                System.out.println("rename read file");
                }
                
                
//            }
//        //outer loop, executes one for each file
//        for(int i = 0; i < files.size();i++){
//
//            System.out.println("Receiving file: " + files.get(i).getName());
//            //create a new fileoutputstream for each new file
//            FileOutputStream fos = new FileOutputStream(files.get(i).getAbsolutePath());
//            //read file
//            while((n = dis.read(buf)) != -1){
//                fos.write(buf,0,n);
//                fos.flush();
//            }
//            fos.close();
//        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

}
