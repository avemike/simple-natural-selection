package models;

import Simulation.Simulation;
import services.Config;

/**
 * Represents a physical instance on a map in Simulation
 */
public abstract class Representative extends Spatial {
    protected final Simulation simulation;
    protected int width;
    protected int height;
    protected String specie_name;

    // Constructors
    Representative(final Simulation simulation, final double x, final double y, int width, int height, String specie_name) {
        super(x, y);

        this.simulation = simulation;
        this.specie_name = specie_name;

        final double size_modifier = Double.parseDouble(Config.get(specie_name + "_size_modifier"));

        setDimensions((int) (width * size_modifier), (int) (height * size_modifier));
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
