package Part3;

import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.io.*;

public class Client {

    Date date;
    // Declare client socket
    Socket clientSocket = null;
    short id;
    // Declare output stream and string to send to server
    DataOutputStream output = null;
    DataInputStream input = null;
    Scanner scanner;

    public Client() {

        date = new Date();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        id = (short) ((60 * minutes) + seconds);

        try {
            clientSocket = new Socket("localhost", 5000);
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());
            scanner = new Scanner(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.writeShort(id);
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInt(int v) {
        try {
            output.writeInt(v);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readInt() {
        int k = 0;
        try {
            k = input.readInt();
            System.out.println(k);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }
}