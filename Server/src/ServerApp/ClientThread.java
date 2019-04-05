package ServerApp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    String kart;
    ClientThread opponent;
    Socket socket;
    Scanner input;
    DataOutputStream output;
    ObjectInputStream objectInput;
    ObjectOutputStream objectOutput;
    boolean isConnected;

    public ClientThread(Socket socket, String kart) {
        this.socket = socket;
        this.kart = kart;
        isConnected = true;
    }

    @Override
    public void run() {
        
        String received;
        System.out.println("Connection established" + socket);
        try {
            input = new Scanner(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            objectOutput = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while(true)
        {
            try {
                //received = input.nextLine();
                //output.println(received);
                if(opponent != null)
                {
                   // received = input.nextLine();
                    Packet packet = (Packet) objectInput.readObject();
                    System.out.println("received");
                    //opponent.output.writeBytes(received);
                    opponent.objectOutput.writeObject(packet);
                    //opponent.output.flush();
                }
                else 
                {
                    //output.println("Waiting for opponent...");
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            } 
          
        }
    }

    public class Packet implements Serializable {
            
        private static final long serialVersionUID = -895922647447501206L;
        float carX;
        float carY;
        float speed;
        int index;

        public Packet(float carX, float carY, float speed, int index)
        {
            this.carX = carX;
            this.carY = carY;
            this.speed = speed;
            this.index = index;
        }
    }

}