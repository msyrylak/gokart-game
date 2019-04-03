package ServerApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;






public class ClientThread implements Runnable {
    String kart;
    ClientThread opponent;
    Socket socket;
    Scanner input;
    PrintWriter output;
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

        while(true)
        {
            try {
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME, YOUR KART IS: " + kart);
                //received = input.nextLine();
                //output.println(received);
                if(opponent != null)
                {
                    received = input.nextLine();
                    System.out.println(received);
                    opponent.output.println(received);
                }
                else 
                {
                    output.println("Waiting for opponent...");
                }
    
            } catch (IOException e) {
                e.printStackTrace();
            } 
          
        }
        // try {
        //     this.input.close();
        //     this.output.close();
            
        // } catch (IOException e) {
        //     //TODO: handle exception
        // } 

    }

    public void sendInfo()
    {

    }
}