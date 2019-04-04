package ClientApp;

// gui
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// // socket connection
// import java.io.*;
// import java.net.*;
// import java.util.Scanner;
// import java.util.concurrent.Executors;

public class ClientPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1L;
    private ImageIcon[] carsWhite;
    private ImageIcon[] carsBlack;
    // private float speed = 0;
    private float angle;
    private int indexWhite;
    private int indexBlack;
    GoKart whiteKart = null;
    GoKart blackKart = null;
    private Vector2 vector = new Vector2();
    private Timer timer;

    // collision boxes
    Rectangle grass;
    Rectangle outerEdge;

    // server connection
    ServerConnection serverConnection = null;

    public ClientPanel() {

        serverConnection = new ServerConnection();
        serverConnection.socketProcessing();
        this.Init();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    // initialise game's features
    public void Init() {
        indexBlack = 0;
        indexWhite = 0;
        // create two objects of gokart class
        whiteKart = new GoKart("white", 0, 425, 500);
        blackKart = new GoKart("black", 0, 425, 550);

        // get cars' images
        carsWhite = whiteKart.getCars();
        carsBlack = blackKart.getCars();

        // add collision boxes for the environment
        grass = new Rectangle(150, 200, 550, 300);
        outerEdge = new Rectangle(50, 100, 750, 500);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color c1 = Color.green;
        g.setColor(c1);
        g.fillRect(150, 200, 550, 300);

        Color c2 = Color.black;
        g.setColor(c2);
        g.drawRect(50, 100, 750, 500); // outer edge
        g.drawRect(150, 200, 550, 300);

        Color c3 = Color.yellow;
        g.setColor(c3);
        g.drawRect(100, 150, 650, 400); // mid lane marker

        Color c4 = Color.white;
        g.setColor(c4);
        g.drawLine(425, 500, 425, 600); // start line

        carsWhite[indexWhite].paintIcon(this, g, (int) whiteKart.carPosX, (int) whiteKart.carPosY);
        carsBlack[indexBlack].paintIcon(this, g, (int) blackKart.carPosX, (int) blackKart.carPosY);

        // add collision boxes to the karts
        whiteKart.AddCollisionBox(carsWhite[indexWhite].getIconWidth(), carsWhite[indexWhite].getIconHeight());
        blackKart.AddCollisionBox(carsWhite[indexBlack].getIconWidth(), carsWhite[indexBlack].getIconHeight());
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {

        myKeyEvent(e, "keyPressed");
        int key = e.getKeyCode();

        if (timer == null) {
            timer = new Timer(100, new TimerHandler());
        }

        switch (key) {
        case KeyEvent.VK_D:
            timer.start();
            if (indexBlack == 0) {
                indexBlack = carsBlack.length - 1;
            } else if (indexBlack > 0) {
                indexBlack--;
            }
            break;
        case KeyEvent.VK_A:
            timer.start();
            if (indexBlack >= carsBlack.length - 1) {
                indexBlack = 0;
            } else {
                indexBlack++;
            }
            break;

        case KeyEvent.VK_W:
            blackKart.speed += 1;
            angle = (float) (Math.toRadians(indexBlack * 22.5));
            vector = vector.BuildVector(-angle);
            blackKart.carPosX += blackKart.speed * vector.x;
            blackKart.carPosY += blackKart.speed * vector.y;
            break;

        case KeyEvent.VK_S:
            blackKart.speed -= 1;
            angle = (float) (Math.toRadians(indexBlack * 22.5));
            vector = vector.BuildVector(-angle);
            blackKart.carPosX -= blackKart.speed * vector.x;
            blackKart.carPosY -= blackKart.speed * vector.y;
            break;

        default:
            break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        myKeyEvent(e, "keyTyped");

    }

    @Override
    public void keyReleased(KeyEvent e) {
        myKeyEvent(e, "keyReleased");

    }

    private void myKeyEvent(KeyEvent e, String text) {

        // move to action performed?
        // simple collision detection
        if (!outerEdge.contains(whiteKart.aabb)) {
            whiteKart.speed = 0;
        } else if (whiteKart.aabb.intersects(grass)) /* || whiteKart.aabb.intersects(outerEdge.) */ {

            whiteKart.speed = 0;

        }
        if (!outerEdge.contains(blackKart.aabb)) {
            blackKart.speed = 0;
        } else if (blackKart.aabb.intersects(grass)) {

            blackKart.speed = 0;

        } else if (blackKart.aabb.intersects(whiteKart.aabb) || whiteKart.aabb.intersects(blackKart.aabb)) {

            // Warning icon made by Twitter from Flaticon <www.flaticon.com>
            ImageIcon icon = new ImageIcon(this.getClass().getResource("Pics/warning.png"));

            // start the game again or close the program
            String[] options = { "Start again", "Close the program" };

            int x = JOptionPane.showOptionDialog(this, "GoKarts crashed!", "Gameover", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
            if (x == 0) {
                Init();
            } else {
                System.exit(ABORT);
            }
        }
    }

    private class TimerHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            serverConnection.sendInformation(blackKart.carPosX, blackKart.carPosY, blackKart.speed, indexBlack);
            repaint();
        }

    }

    // add that to utils?
    private class Vector2 {
        public float x;
        public float y;

        public Vector2() {
            this.x = 0.0f;
            this.y = 0.0f;
        }

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 BuildVector(final float direction) {
            return new Vector2((float) Math.cos(direction), (float) Math.sin(direction));
        }

    }


}
