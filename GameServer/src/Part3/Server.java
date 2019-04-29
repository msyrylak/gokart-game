package Part3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    ServerSocket server;
    // array of players
    Player[] players;
    String[] playerColours = { "white", "black" };
    ExecutorService serviceGame;
    final static int NUMBER_OF_PLAYERS = 2;
    final static int PORT = 5000;

    public Server() {

        // create array of players
        players = new Player[NUMBER_OF_PLAYERS];
        serviceGame = Executors.newFixedThreadPool(NUMBER_OF_PLAYERS);

        try {
            server = new ServerSocket(PORT);
            System.out.print("Server is running... \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            try {
                players[i] = new Player(server.accept(), playerColours[i]);
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
        String colour;
        DataInputStream input;
        DataOutputStream output;
        short id;
        boolean shouldRun = false;

        public Player(Socket socket, String colour) {
            this.socket = socket;
            this.colour = colour;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                id = input.readShort();
                System.out.println("Player " + id + " connected");
                output.writeUTF(colour);
                output.flush();
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
                    byte[] playerData = new byte[13];
                    input.readFully(playerData);
                    sendObjectData(playerData);

                } catch (IOException e) {
                    // e.printStackTrace();
                    System.out.println("Client " + id + " disconnected!");
                    for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                        if (this.equals(players[i])) {

                            try {
                                players[i].input.close();
                                players[i].output.close();
                                players[i].socket.close();
                                
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }

        public void sendObjectData(byte[] v) {
            for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                if (!this.equals(players[i])) {
                    try {
                        players[i].output.write(v);
                    } catch (IOException e) {
                        // e.printStackTrace();
                        System.out.println("Can't send anything!");
                        //shouldRun = false;
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