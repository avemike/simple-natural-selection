package models;

import Simulation.Simulation;
import services.Config;

public abstract class Plant extends GraphicalRepresentative {
    public boolean is_dead = false;
    public boolean isEdible;
    protected double kcal = 0;

    public Plant(final Simulation simulation, final double x, final double y, final String path, final String specie_name) {
        super(simulation, x, y, Integer.parseInt(Config.get("plants_pixel_size")), path, specie_name);
    }

    protected void death() {
        is_dead = true;
    }
}
