package Part3;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.*;

public class Client extends JPanel {

    private static final long serialVersionUID = -5695794244004318392L;
    boolean ready = false;
    Date date;
    // Declare client socket
    Socket clientSocket = null;
    short id;
    String goKartColour;
    // Declare output stream and string to send to server
    DataInputStream input = null;
    DataOutputStream output = null;

    // gui for the client side
    JLabel playerColour = new JLabel();
    JLabel generalInfo = new JLabel();
    JLabel connectionStatus = new JLabel();

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
            System.out.println("Waiting for colour " + date.getHours() + date.getMinutes());
            goKartColour = input.readUTF();
            System.out.println("Received colour " + date.getHours() + date.getMinutes());
            ready = true;
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Here");
        }

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        playerColour.setText("You are the " + goKartColour + " GoKart.");
        connectionStatus.setText("Connected to the server.");

        this.add(playerColour);
        this.add(connectionStatus);
        this.add(generalInfo);
    }

    public void sendBytes(float posX, float posY, int index, byte crash) {
        byte[] positionX = ByteBuffer.allocate(4).putFloat(posX).array();
        byte[] positionY = ByteBuffer.allocate(4).putFloat(posY).array();
        byte[] kartIndex = ByteBuffer.allocate(4).putInt(index).array();
        byte[] crashStatus = ByteBuffer.allocate(1).put(crash).array();

        ByteArrayOutputStream myStream = new ByteArrayOutputStream();
        try {
            myStream.write(positionX);
            myStream.write(positionY);
            myStream.write(kartIndex);
            myStream.write(crashStatus);
            byte[] kartData = myStream.toByteArray();

            output.write(kartData);
            output.flush();
        } catch (IOException e) {
            // e.printStackTrace();
            connectionStatus.setText("Lost Connection!");
        }
    }

    public byte[] receiveBytes() {
        byte[] kartData = null;
        try {
            int length = input.available();
            if (length > 0) {
                kartData = new byte[length];
                try {
                    input.readFully(kartData);
                } catch (IOException e) {
                    generalInfo.setText("Opponent disconnected");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return kartData;
    }
}