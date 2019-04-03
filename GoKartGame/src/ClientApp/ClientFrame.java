package ClientApp;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private ClientPanel panel;

public ClientFrame()
{
    this.setTitle("Go Karts");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setBounds(100,100,850,650);

    Container cp = this.getContentPane();

    panel = new ClientPanel();
    panel.setBounds(0,0,850,650);
    cp.add(panel);
}
    
}