package socket.forwarding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author user
 */
public class ForwardThread extends Thread {

    private static final int BUFFER_SIZE = 8192;

    InputStream mInputStream;
    OutputStream mOutputStream;
    ClientThread mParent;

    public ForwardThread(ClientThread aParent, InputStream aInputStream, OutputStream aOutputStream) {
        mParent = aParent;
        mInputStream = aInputStream;
        mOutputStream = aOutputStream;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];

        try {

//            int count = 0;
//            count = mInputStream.available();
//            System.out.println("read " + count + " from client ");

            while (true) {
                int bytesRead = mInputStream.read(buffer);
                if (bytesRead == -1) {
                    break; // End of stream is reached --> exit 
                }
                mOutputStream.write(buffer, 0, bytesRead);
                mOutputStream.flush();
            }
        } catch (IOException e) {
            // Read/write failed --> connection is broken 
        }

        // Notify parent thread that the connection is broken 
        mParent.connectionBroken();
    }
}
