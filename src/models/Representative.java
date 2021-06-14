package models;

import Simulation.Simulation;

/**
 * Represents a physical instance on a map in Simulation
 */
public abstract class Representative extends Spatial {
    protected final Simulation simulation;
    protected int width;
    protected int height;

    // Constructors
    Representative(final Simulation simulation, final int x, final int y, final int width, final int height) {
        super(x, y);

        this.simulation = simulation;
        setDimensions(width, height);
    }

    Representative() {
        super(0, 0);

        this.simulation = null;
        setDimensions(0, 0);
    }


    // Setters
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
