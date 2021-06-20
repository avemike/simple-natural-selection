package models;

import Simulation.Simulation;
import services.Config;

public abstract class Plant extends GraphicalRepresentative {
    public boolean is_dead = false;
    protected double kcal = 0;

    public Plant(final Simulation simulation, final int x, final int y, final String path) {
        super(simulation, x, y, Integer.parseInt(Config.get("plants_pixel_width")), Integer.parseInt(Config.get("plants_pixel_height")), path);
    }

    protected void death() {
        is_dead = true;
    }
}
