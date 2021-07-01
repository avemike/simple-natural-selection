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
    protected final Attributes attribs;
    protected Interaction interaction = null;
    protected Movement movement = null;
    protected Needs needs = null;

    public Animal(final double x, final double y, final double size, final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);

        attribs = new Attributes(size, specie_name);
        movement = new Movement(this, attribs, needs, interaction);
        needs = new Needs(this, attribs, movement, interaction);
        interaction = new Interaction(this, attribs, needs, movement);

    }

    public abstract void reproduce(final Animal animal) throws Exception;

    // = = = = = = //
    //   Public    //
    // = = = = = = //
    public void death() {
        needs.death();
    }

    public void act() throws Exception {
        interaction.act();
    }

    public void fillReproduction() {
        needs.fillReproduction();
    }
    // = = = = = = //
    //   Getters   //
    // = = = = = = //

    public double getPower() {
        return attribs.power;
    }

    public double getSize() {
        return attribs.size;
    }

    public double getUsableKcal() {
        return needs.getUsableKcal();
    }

    public boolean getSex() {
        return attribs.sex;
    }

    public String getSpecieName() {
        return attribs.specie_name;
    }

    public boolean isDead() {
        return needs.is_dead;
    }


}
