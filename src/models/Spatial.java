package models;

import utils.Position;

/**
 * Represents an instance with position on map
 */
public abstract class Spatial {
    protected Position coords;

    // Constructor
    Spatial(int x, int y) {
        coords = new Position(x, y);
    }

    Spatial(Position pos) {
        coords = new Position(pos.x, pos.y);
    }

    // Getters
    public double getX() {
        return coords.x;
    }

    public void setX(double x) {
        coords.x = x;
    }

    public double getY() {
        return coords.y;
    }

    public void setY(double y) {
        coords.y = y;
    }

    public Position getPosition() {
        return coords;
    }

    // Setters
    public void changeCoords(int x, int y) {
        coords.x = x;
        coords.y = y;
    }
}
