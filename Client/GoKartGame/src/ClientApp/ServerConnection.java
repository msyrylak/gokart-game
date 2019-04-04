package ClientApp;

// socket connection
import java.io.*;
import java.net.*;
import java.util.Scanner;
//import java.util.concurrent.Executors;

public class ServerConnection {

    Socket clientSocket = null;
    DataOutputStream os = null;
    DataInputStream is = null;
    Scanner scn = null;

    public ServerConnection() {
        try {
            clientSocket = new Socket("localhost", 5000);
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // socket connection
    public void socketProcessing() // throws UnknownHostException, IOException
    {
        scn = new Scanner(System.in);

        //try {
            // clientSocket = new Socket("localhost", 5000);
            // DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
            // DataInputStream is = new DataInputStream(clientSocket.getInputStream());

            if (clientSocket != null && os != null && is != null) {
                try {

                    // sendMessage thread
                    Thread sendMessage = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                // read the message to deliver.
                                String msg = scn.nextLine();

                                try {
                                    // write to the output stream
                                    msg += "\n";
                                    os.writeBytes(msg);
                                    os.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    byte[] bytes = new byte[8];

                    // readMessage thread
                    Thread readMessage = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (true) {
                                try {
                                    // read the message sent to this client
                                    is.readFully(bytes);
                                    System.out.println("test");
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    sendMessage.start();
                    readMessage.start();

                } catch (Exception e) {
                    System.err.println("Trying to connect to unknown host: " + e);
                }
            }

        // } catch (UnknownHostException e) {
        //     System.out.println("Don't know about host: hostname");
        // } catch (IOException e) {
        //     System.err.println("Couldn't get I/O for the connection to: hostname");
        // }

    }

    public void sendInformation(float carX, float carY, float speed, int index) {

    }

    public void receiveInformation() {

    }

    public void close() {
        try {
            this.clientSocket.close();
            this.scn.close();
            this.os.close();
            this.is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    
}