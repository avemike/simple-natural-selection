package models;

import models.animal.AnimalInteraction;

/**
 * @apiNote CHAIN OF INHERITANCE
 * AnimalAttributes -> AnimalNeeds -> AnimalMovement -> AnimalInteraction -> Animal
 */
public abstract class Animal extends AnimalInteraction {
    public Animal(final double x, final double y, final double size, final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);
    }
}
