package ServerApp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    String kart;
    ClientThread opponent;
    Socket socket;
    Scanner input;
    DataOutputStream output;
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
                    received = input.nextLine();
                    System.out.println(received);
                    opponent.output.writeBytes(received);
                    opponent.output.flush();
                }
                else 
                {
                    //output.println("Waiting for opponent...");
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            } 
          
        }
        // try {
        //     this.input.close();
        //     this.output.close();
            
        // } catch (IOException e) {
        //     handle exception
        // } 

    }
}