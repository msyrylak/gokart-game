package ServerApp;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameServer  {

    ClientThread[] players;
    ServerSocket server;
    ClientThread opponent;
    ClientThread thisPlayer;
    private final static String whiteKart = "WHITE";
    private final static String blackKart = "BLACK";
    private Condition otherPlayerConnected;
    private Lock gameLock;
    private ExecutorService runGame;

    public GameServer()
    {
        runGame = Executors.newFixedThreadPool(2);
        gameLock = new ReentrantLock();

        otherPlayerConnected = gameLock.newCondition();

        players = new ClientThread[2];
    }


}