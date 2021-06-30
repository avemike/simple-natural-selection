package models.animal;

import services.Config;

/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 */
public class Attributes {
    public boolean sex;
    public boolean is_herbivore = false;
    public boolean is_meat_eater = false;
    public double power;
    public double size;
    public double speed;
    public double sight_range;
    public double interaction_range;
    public String specie_name;

    public Attributes(final double size, final String specie_name) {
        this.size = size;
        this.specie_name = specie_name;

        power = Double.parseDouble(Config.get(specie_name + "_power"));
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
