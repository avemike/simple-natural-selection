package models;

/**
 * Represents an instance with position on map
 */
public abstract class Spatial {
    protected double x;
    protected double y;


    // Constructor
    Spatial(int x, int y) {
        changeCoords(x, y);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Setters
    public void changeCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
