package models.animal;

import models.GraphicalRepresentative;

/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 */
public abstract class AnimalAttributes extends GraphicalRepresentative {
    protected boolean sex;
    protected boolean is_herbivore = false;
    protected boolean is_meat_eater = false;
    protected double power;
    protected double size;
    protected double speed;
    protected double sight_range;
    protected double interaction_range;

    public AnimalAttributes(final double x, final double y, final double size,
                            final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);
        this.size = size;

        power = size;
        interaction_range = 16 + size;
    }

    // Getters
    public double getPower() {
        return power;
    }

    public double getSize() {
        return size;
    }
}
