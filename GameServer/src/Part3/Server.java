package Part3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class Server {

    ServerSocket server;
    // array of players
    Player[] players;
    ExecutorService serviceGame;
    private Lock gameLock;
    final static int NUMBER_OF_PLAYERS = 2;

    public Server() {

        // create array of players
        players = new Player[NUMBER_OF_PLAYERS];
        serviceGame = Executors.newFixedThreadPool(NUMBER_OF_PLAYERS);

        try {
            server = new ServerSocket(5000);
            System.out.print("Server is running... \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            try {
                players[i] = new Player(server.accept());
                serviceGame.execute(players[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                players[i].setRunStatus(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public class Player implements Runnable {
        Socket socket;
        DataInputStream input;
        DataOutputStream output;
        Scanner scanner;
        short id;
        boolean shouldRun = false;

        public Player(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                id = input.readShort();
                System.out.println("Player " + id + " connected");

                while (shouldRun == false) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (shouldRun) {
                try {
                int  v = input.readInt();
                sendObjectData(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendObjectData(int v) {
            for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                if (!this.equals(players[i])) {
                    try {
                        players[i].output.writeInt(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        public void setRunStatus(boolean status)
        {
            this.shouldRun = status;
        }
    }
}