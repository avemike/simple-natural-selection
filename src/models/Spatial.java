package models;

public abstract class Spatial {
    protected int x;
    protected int y;


    // Constructor
    Spatial(int x, int y) {
        changeCoords(x, y);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setters
    public void changeCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
