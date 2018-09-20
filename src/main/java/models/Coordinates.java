package models;

/**
 * Support method for storage two coordinates in one class.
 */
public class Coordinates {
    public Coordinates(int tempX, int tempY){
        setX(tempX);
        setY(tempY);
    }
    private void setX(int tempX){
        this.X = tempX;
    }
    private void setY(int tempY){
        this.Y = tempY;
    }
    public int getX(){
        return this.X;
    }
    public int getY(){
        return this.Y;
    }

    private int X;
    private int Y;
}
