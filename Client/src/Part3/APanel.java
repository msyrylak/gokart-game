package Part3;

import java.awt.event.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.awt.*;
import javax.swing.*;

public class APanel extends JPanel implements KeyListener, ActionListener {
    private static final long serialVersionUID = -4828340630943595544L;
    private final static String FIRST_CAR = "white";
    private final static String SECOND_CAR = "black";
    private final int TOTAL_INDEX = 15;
    private final int startingIndex = 0;
    private final int REFRESH_RATE = 50;
    // private JLabel playerColour = new JLabel();
    private Timer timer;
    GoKart playerKart = null;
    GoKart opponentKart = null;
    Client client;
    // aabb for environment
    Rectangle grass;
    Rectangle outerEdge;

    // packet info
    byte[] data;

    public APanel() {

        this.setBounds(0, 0, 850, 650);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer = new Timer(REFRESH_RATE, this);
        timer.start();
        client = new Client();
        this.add(client);
        while (!client.ready) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }                
        }
        if (client.goKartColour.equals(FIRST_CAR)) {
            playerKart = new GoKart(FIRST_CAR, startingIndex, 425, 500);
            opponentKart = new GoKart(SECOND_CAR, startingIndex, 425, 550);
        } else {
            playerKart = new GoKart(SECOND_CAR, startingIndex, 425, 550);
            opponentKart = new GoKart(FIRST_CAR, startingIndex, 425, 500);
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Color c1 = Color.green;
        g.setColor(c1);
        g.fillRect(150, 200, 550, 300);
        Color c2 = Color.black;
        g.setColor(c2);
        g.drawRect(50, 100, 750, 500); // outer edge
        g.drawRect(150, 200, 550, 300); // inner edge
        Color c3 = Color.yellow;
        g.setColor(c3);
        g.drawRect(100, 150, 650, 400); // mid-lane
        Color c4 = Color.white;
        g.setColor(c4);
        g.drawLine(425, 500, 425, 600); // start line

        grass = new Rectangle(150, 200, 550, 300);
        outerEdge = new Rectangle(50, 100, 750, 500);
        playerKart.AddCollisionBox(50, 50);
        opponentKart.AddCollisionBox(50, 50);

        playerKart.getCurrentImage().paintIcon(this, g, (int) playerKart.posX, (int) playerKart.posY);
        opponentKart.getCurrentImage().paintIcon(this, g, (int) opponentKart.posX, (int) opponentKart.posY);

        client.sendBytes(playerKart.posX, playerKart.posY, playerKart.index);
        
        if((data = client.receiveBytes()) != null)
        {
            client.generalInfo.setText("Opponent found!");            
            byte[] position = Arrays.copyOfRange(data, 0, 4);
            byte[] position2 = Arrays.copyOfRange(data, 4, 8);
            byte[] position3 = Arrays.copyOfRange(data, 8, 12);
    
            opponentKart.posX = ByteBuffer.wrap(position).getFloat();
            opponentKart.posY = ByteBuffer.wrap(position2).getFloat();
            opponentKart.index = ByteBuffer.wrap(position3).getInt();    
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {

        case KeyEvent.VK_RIGHT:
            if (playerKart.index == 0) {
                playerKart.index = TOTAL_INDEX;
            } else if (playerKart.index > 0) {
                playerKart.index--;
            }
            break;
        case KeyEvent.VK_LEFT:
            if (playerKart.index >= TOTAL_INDEX) {
                playerKart.index = 0;
            } else {
                playerKart.index++;
            }
            break;
        case KeyEvent.VK_UP:
            playerKart.moveForward();
            break;

        case KeyEvent.VK_DOWN:
            playerKart.moveBackwards();
            break;

        default:
            break;
        }

        // simple collision detection
        if (!outerEdge.contains(playerKart.aabb))
        {
        playerKart.setSpeed(0);
        }
        else if (playerKart.aabb.intersects(grass)) /*||
        whiteKart.aabb.intersects(outerEdge.)*/ {

        playerKart.setSpeed(0);

        }
        if (!outerEdge.contains(opponentKart.aabb))
        {
        opponentKart.setSpeed(0);
        }
        else if (opponentKart.aabb.intersects(grass)) {

        opponentKart.setSpeed(0);

        }
        else if (opponentKart.aabb.intersects(playerKart.aabb) ||
        playerKart.aabb.intersects(opponentKart.aabb)) {

        // Warning icon made by Twitter from Flaticon <www.flaticon.com>
        ImageIcon icon = new
        ImageIcon(this.getClass().getResource("Pics/warning.png"));

        // start the game again or close the program
        String[] options = { "Start again", "Close the program" };

        int x = JOptionPane.showOptionDialog(this, "GoKarts crashed!", "Gameover",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
        if (x == 0) {
        reset();
        } else {
        System.exit(ABORT);
        }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void reset() {
        playerKart.setSpeed(0);
        opponentKart.setSpeed(0);

        if (client.goKartColour.equals(FIRST_CAR)) {
            playerKart.index = startingIndex;
            playerKart.posX = 425;
            playerKart.posY = 500;

            opponentKart.index = startingIndex;
            opponentKart.posX = 425;
            opponentKart.posY = 550;
        } else {
            playerKart.index = startingIndex;
            playerKart.posX = 425;
            playerKart.posY = 550;

            opponentKart.index = startingIndex;
            opponentKart.posX = 425;
            opponentKart.posY = 500;
        }

    }
}