package models;

import models.animal.Attributes;
import models.animal.Interaction;
import models.animal.Movement;
import models.animal.Needs;

/**
 * @apiNote CHAIN OF INHERITANCE
 * AnimalAttributes -> AnimalNeeds -> AnimalMovement -> AnimalInteraction -> Animal
 */
public abstract class Animal extends GraphicalRepresentative {
    public final Attributes attribs;
    public Interaction interaction = null;
    public Movement movement = null;
    public Needs needs = null;

    public Animal(final double x, final double y, final double size, final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);

        attribs = new Attributes(size, specie_name);
        movement = new Movement(this, attribs, needs, interaction);
        needs = new Needs(this, attribs, movement, interaction);
        interaction = new Interaction(this, attribs, needs, movement);

    }

    public abstract void reproduce(final Animal animal) throws Exception;
}
