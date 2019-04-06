package Part3;

import java.awt.Rectangle;
import javax.swing.*;

public class GoKart {
    String colour;
    float posX;
    float posY;
    float speed = 0;
    float direction;
    int index;
    ImageIcon currentImage;
    Vector2 directionVector = new Vector2();
    public Rectangle aabb;

    public GoKart(String colour, int index, float posX, float posY) {
        this.colour = colour;
        this.index = index;
        this.posX = posX;
        this.posY = posY;
    }

    public ImageIcon getCurrentImage() {
        currentImage = new ImageIcon(getClass().getResource("Pics/" + colour + index + ".png"));
        return currentImage;
    }

    public void getDirection() {

    }

    public void setSpeed(float value)
    {
        this.speed = value;
    }

    public float getSpeed()
    {
        return this.speed;
    }

    public void moveForward() {
        if (speed < 100) {
            speed += 1;
        }
        float angle = (float) (Math.toRadians(index * 22.5));
        directionVector = directionVector.BuildVector(-angle);
        posX += speed * directionVector.x;
        posY += speed * directionVector.y;
    }

    public void moveBackwards() {
        if (speed > 0) {
            speed -= 1;
        }
        float angle = (float) (Math.toRadians(index * 22.5));
        directionVector = directionVector.BuildVector(-angle);
        posX -= speed * directionVector.x;
        posY -= speed * directionVector.y;
    }

    public void AddCollisionBox(int width, int height) {
        this.aabb = new Rectangle((int) this.posX, (int) this.posY, width, height);
    }

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