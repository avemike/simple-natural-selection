package models;

import utils.Position;

import java.util.Vector;

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

    protected Spatial getClosestInstance(final Vector<Spatial> objs) {
        int which_one = 0;
        double closest_range = Position.getRange(coords, objs.get(0).getPosition());

        for (int i = 1, max = objs.size(); i < max; i++) {
            final double current_range = Position.getRange(coords, objs.get(0).getPosition());
            if (current_range < closest_range) {
                which_one = i;
                closest_range = current_range;
            }
        }

        return objs.get(which_one);
    }

    // Setters
    public void changeCoords(int x, int y) {
        coords.x = x;
        coords.y = y;
    }
}
