package models;

import interfaces.Edible;
import services.Config;

public abstract class Plant extends GraphicalRepresentative implements Edible {
    public boolean is_dead = false;
    public boolean isEdible;
    protected double kcal = 0;

    public Plant(final double x, final double y, final String path, final String specie_name) {
        super(x, y, Integer.parseInt(Config.get("plants_pixel_size")), path, specie_name);
    }

    public void death() {
        is_dead = true;
    }

    public double getKcal() {
        return kcal;
    }
}
