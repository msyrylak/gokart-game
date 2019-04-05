package Part2;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class APanel extends JPanel implements KeyListener, ActionListener {
    private static final long serialVersionUID = -4828340630943595544L;
    private final static String FIRST_CAR = "white";
    private final static String SECOND_CAR = "black";
    private final int TOTAL_INDEX = 15;
    private final int startingIndex = 0;
    private final int REFRESH_RATE = 50;
    private Timer timer;
    GoKart whiteKart = new GoKart(FIRST_CAR, startingIndex, 425, 500);
    GoKart blackKart = new GoKart(SECOND_CAR, startingIndex, 425, 550);

    // aabb for environment
    Rectangle grass;
    Rectangle outerEdge;

    public APanel() {

        this.setBounds(0, 0, 850, 650);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer = new Timer(REFRESH_RATE, this);
        timer.start();
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
        whiteKart.AddCollisionBox(50, 50);
        blackKart.AddCollisionBox(50, 50);

        whiteKart.getCurrentImage().paintIcon(this, g, (int) whiteKart.posX, (int) whiteKart.posY);
        blackKart.getCurrentImage().paintIcon(this, g, (int) blackKart.posX, (int) blackKart.posY);
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
        case KeyEvent.VK_D:
            if (blackKart.index == 0) {
                blackKart.index = TOTAL_INDEX;
            } else if (blackKart.index > 0) {
                blackKart.index--;
            }
            break;
        case KeyEvent.VK_A:
            if (blackKart.index >= TOTAL_INDEX) {
                blackKart.index = 0;
            } else {
                blackKart.index++;
            }
            break;

        case KeyEvent.VK_W:
            blackKart.moveForward();
            break;

        case KeyEvent.VK_S:
            blackKart.moveBackwards();
            break;

        case KeyEvent.VK_RIGHT:
            if (whiteKart.index == 0) {
                whiteKart.index = TOTAL_INDEX;
            } else if (whiteKart.index > 0) {
                whiteKart.index--;
            }
            break;
        case KeyEvent.VK_LEFT:
            if (whiteKart.index >= TOTAL_INDEX) {
                whiteKart.index = 0;
            } else {
                whiteKart.index++;
            }
            break;
        case KeyEvent.VK_UP:
            whiteKart.moveForward();
            break;

        case KeyEvent.VK_DOWN:
            whiteKart.moveBackwards();
            break;

        default:
            break;
        }

        // simple collision detection
        if (!outerEdge.contains(whiteKart.aabb))
        {
        whiteKart.setSpeed(0);
        }
        else if (whiteKart.aabb.intersects(grass)) /*||
        whiteKart.aabb.intersects(outerEdge.)*/ {

        whiteKart.setSpeed(0);

        }
        if (!outerEdge.contains(blackKart.aabb))
        {
        blackKart.setSpeed(0);
        }
        else if (blackKart.aabb.intersects(grass)) {

        blackKart.setSpeed(0);

        }
        else if (blackKart.aabb.intersects(whiteKart.aabb) ||
        whiteKart.aabb.intersects(blackKart.aabb)) {

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

    public void reset()
    {
        whiteKart.setSpeed(0);
        blackKart.setSpeed(0);

        whiteKart.index = startingIndex;
        blackKart.index = startingIndex;

        whiteKart.posX = 425;
        whiteKart.posY = 500;

        blackKart.posX = 425;
        blackKart.posY = 550;
    }
}