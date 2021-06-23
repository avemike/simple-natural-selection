package models;

import utils.Position;

import java.util.Vector;

/**
 * Represents an instance with position on map
 */
public abstract class Spatial {
    protected Position coords;

    // Constructor
    Spatial(double x, double y) {
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

    protected Animal getClosestAnimal(final Vector<Animal> objs) {
        if (objs.size() == 0) return null;

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

    protected Plant getClosestPlant(final Vector<Plant> objs) {
        if (objs.size() == 0) return null;

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

    public Position calcNextStep(double angle, double step_range) {
        if (angle > 360) angle -= 360;
        if (angle < 0) angle += 360;

        final var horizontal_diff = Math.cos(angle) * step_range;
        final var vertical_diff = Math.sin(angle) * step_range;

        return new Position(coords.x + horizontal_diff, coords.y + vertical_diff);
    }

    // Setters
    public void changeCoords(int x, int y) {
        coords.x = x;
        coords.y = y;
    }
}
