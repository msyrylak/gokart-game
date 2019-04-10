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
            clientSocket = new Socket("localhost", 5000);
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
            e.printStackTrace();
        }
    }

    public void sendFloat(float v) {
        try {
            output.writeFloat(v);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float receiveFloat() {
        float k = 0;
        try {
            k = input.readFloat();
            System.out.println(k);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }

    public void sendInt(int v) {
        try {
            output.writeInt(v);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int receiveInt() {
        int k = 0;
        try {
            k = input.readInt();
            System.out.println(k);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }

    public void sendColour(String v) {
        try {
            output.writeUTF(v);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveColour() {
        String v = "";
        try {
            v = input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
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
            e.printStackTrace();
        }


    }

    public byte[] receiveBytes()
    {
        byte[] kartData = new byte[12];
        try {
            input.readFully(kartData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kartData;
    }
    
    
    
    // public void sendGoKart(GoKart kart) {

    //     try {
    //         output.writeObject(kart);
    //         output.flush();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // public GoKart readGoKart() {
    //     GoKart k = null;
    //     try {
    //         k = (GoKart) input.readObject();
    //     } catch (ClassNotFoundException e) {
    //         e.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return k;
    // }

}