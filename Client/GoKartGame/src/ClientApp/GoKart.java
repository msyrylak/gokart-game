package ClientApp;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;


// todo: add getters and setters

public class GoKart implements Serializable {

    private static final long serialVersionUID = 1L;
    private ImageIcon[] cars = new ImageIcon[16];
    public String carColour;
    public float carPosX = 0;
    public float carPosY = 0;
    public float speed = 0;
    public Rectangle aabb;


public GoKart(String colour, float speed, float carPosX, float carPosY)
{
    this.carColour = colour;
    this.speed = speed;
    this.carPosX = carPosX;
    this.carPosY = carPosY;
    this.LoadCars();
    //this.aabb = new Rectangle(carPosX, carPosY, cars[1].getIconHeight(),  );
}

public ImageIcon[] getCars()
{
    return cars.clone();
}


public void LoadCars() {
    
    for (int i = 0; i < cars.length; i++) {
        try {
            cars[i] = new ImageIcon(this.getClass().getResource("Pics/" + carColour + i + ".png"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

public void AddCollisionBox(int width, int height)
{
    this.aabb = new Rectangle((int)this.carPosX, (int)this.carPosY, width, height);
}

}