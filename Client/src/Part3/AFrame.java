package Part3;

import javax.swing.JFrame;

public class AFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    public AFrame()
    {
        this.setTitle("GoKart Game");
        APanel panel = new APanel();
        this.setBounds(0, 0, 850, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
    }
}