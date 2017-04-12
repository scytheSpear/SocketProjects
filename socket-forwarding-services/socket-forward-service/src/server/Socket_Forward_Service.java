package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Socket_Forward_Service {

    public static void main(String[] args) {

        Forward_Client forward_ssh = new Forward_Client("172.16.16.235", 3333, 3333);
        forward_ssh.start();

    }
}
