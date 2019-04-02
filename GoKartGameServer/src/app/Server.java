package app;

import java.io.*;
import java.net.*;

public class Server
{
    public static void main(String[] args) throws Exception {

        try {
           // create server socket
            ServerSocket localhost = new ServerSocket(5000);
            System.out.println("Server is running");
            Socket firstSocket = localhost.accept();

            // create game server object and pass the connection
            GameServer firstServer = new GameServer(firstSocket);
            Thread t1 = new Thread(firstServer);
            System.out.println("First client: "+ firstSocket.getInetAddress());
            t1.start();
            Socket secondSocket = localhost.accept();
            GameServer secondServer = new GameServer(secondSocket);
            Thread t2 = new Thread(secondServer);
            System.out.println("Second client: "+ secondSocket.getInetAddress());
            t2.start();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
