package app;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class GameServer implements Runnable {

    Socket server = null;

    public GameServer(Socket localhost)
    {
       this.server = localhost;
    }
    
    
    @Override
    public void run() {
        
        // client socket
        //ServerSocket service = null;
        //Socket server = null;

        // input stream and string to store message from client
        BufferedReader is;
        String line;

        // outpu stream to client
        DataOutputStream os;

        // create a server socket to listen and accept connections
        try {
            if (server != null) {
                System.out.println("Connection established");
                is = new BufferedReader(new InputStreamReader(server.getInputStream()));
                os = new DataOutputStream(server.getOutputStream());
                if ((line = is.readLine()) != null) {
                    os.writeBytes("Hello client\n");
                }
                // Comment out/remove the stream and socket closes if server is to remain live.
                // os.close();
                // is.close();
                // server.close();                    
            }
        } catch (IOException e) {
            System.out.println(e);
        }        
    }
}