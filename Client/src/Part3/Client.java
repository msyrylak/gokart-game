package Part3;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JPanel;

import java.io.*;

public class Client extends JPanel {

    private static final long serialVersionUID = -5695794244004318392L;
    Date date;
    // Declare client socket
    Socket clientSocket = null;
    short id;
    String goKartColour;
    // Declare output stream and string to send to server
    DataInputStream input = null;
    DataOutputStream output = null;
    Scanner scanner;

    public Client() {

        date = new Date();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        id = (short) ((60 * minutes) + seconds);

        try {
            clientSocket = new Socket(InetAddress.getByName("localhost"), 5000);
            output = new DataOutputStream(clientSocket.getOutputStream());
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.writeShort(id);
            output.flush();
            goKartColour = input.readUTF();

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Here");
        }
    }

    public void sendBytes( float posX, float posY, int index) {
        //byte[] kartColour = colour.getBytes();
        byte[] positionX = ByteBuffer.allocate(4).putFloat(posX).array();
        byte[] positionY = ByteBuffer.allocate(4).putFloat(posY).array();
        byte[] kartIndex = ByteBuffer.allocate(4).putInt(index).array();

        ByteArrayOutputStream myStream = new ByteArrayOutputStream();
        try {
            //myStream.write(kartColour);
            myStream.write(positionX);
            myStream.write(positionY);
            myStream.write(kartIndex);
            byte[] kartData = myStream.toByteArray();
            //myStream.flush();
            
            output.write(kartData);
            output.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Lost connection!");
        }
    }

    public byte[] receiveBytes()
    {
        byte[] kartData = new byte[12];
        try {
            input.readFully(kartData);
        } catch (IOException e) {

            System.out.println("Opponent disconnected");
        }
        return kartData;
    }
    
}