package models;

import Simulation.Simulation;
import models.animal.AnimalInteraction;

/**
 * @apiNote CHAIN OF INHERITANCE
 * AnimalAttributes -> AnimalNeeds -> AnimalMovement -> AnimalInteraction -> Animal
 */
public abstract class Animal extends AnimalInteraction {
    public Animal(final Simulation simulation, final double x, final double y, final double size, final String path, final String specie_name) {
        super(simulation, x, y, (int) size, path, specie_name);
    }
}
