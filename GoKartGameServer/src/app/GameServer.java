package app;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class GameServer {
    public static void main(String[] args) throws Exception {

        // client socket
        ServerSocket service = null;
        Socket server = null;

        // input stream and string to store message from client
        BufferedReader is;
        String line;

        // outpu stream to client
        DataOutputStream os;

        try {
            service = new ServerSocket(5000);
            System.out.println("Server is running...");
        } catch (IOException e) {
            System.out.println(e);
        }

        // create a server socket to listen and accept connections
        try {
            server = service.accept();
            System.out.println("Connection established");
            is = new BufferedReader(new InputStreamReader(server.getInputStream()));
            os = new DataOutputStream(server.getOutputStream());
            if ((line = is.readLine()) != null) {
                os.writeBytes("Hello client\n");
            }
            // Comment out/remove the stream and socket closes if server is to remain live.
            os.close();
            is.close();
            server.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}