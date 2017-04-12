/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettofiles.sever;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class SendFile {

    Socket socket = null;
    
    public SendFile(String host) throws IOException {
        socket = new Socket(host, 6666);
    }

    public void read() {
        File folder = new File("/itsys-data/");
        System.out.println("read from dir");
        String[] pathlist = folder.list();
        ArrayList<String> pathArrayList = new ArrayList<String>();

        for (String a : pathlist) {
            if (a.endsWith(".txt")) {
                System.out.println("add " + a + " to list");
                pathArrayList.add("/itsys-data/" + a);
            }
        }
        pathlist = pathArrayList.toArray(new String[0]);
        Arrays.sort(pathlist);

        for (int i = 0; i < pathlist.length; i++) {
            File file = new File(pathlist[i]);
            if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println("read file:" + file.getName());

                try {
                    long length = file.length();
                    byte[] bytes = new byte[16 * 1024];
                    InputStream in = new FileInputStream(file);
                    OutputStream out = socket.getOutputStream();

                    int count;
                    while ((count = in.read(bytes)) > 0) {
                        out.write(bytes, 0, count);
                    }

                    out.close();
                    in.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                file.renameTo(new File(file.getAbsolutePath() + System.currentTimeMillis() + ".read"));
                System.out.println("rename read file");
            }
        }

    }

}
