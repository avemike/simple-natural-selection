package models.animal;

import models.Animal;
import models.Plant;
import services.Config;
import simulation.Simulation;
import utils.Position;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 * @apiNote `Power` and `Drain rates` are calculated based on size
 * @apiNote hunger is filled by dividing ate usable eaten instance kcal by overall kcal
 */
public class Needs {
    protected final double usable_kcal;
    final Attributes attribs;
    final Interaction interaction;
    final Movement movement;
    final Animal animal;
    public boolean is_dead = false;
    public double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    public double kcal;
    // Needs expressed as percentage
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));
    protected double hunger_stable = Double.parseDouble(Config.get("animals_stable_hunger"));
    protected double thirst_stable = Double.parseDouble(Config.get("animals_stable_thirst"));

    public Needs(final Animal animal, final Attributes attribs, final Movement movement, final Interaction interaction) {
        kcal = Double.parseDouble(Config.get(attribs.specie_name + "_init_kcal"));
        usable_kcal = Double.parseDouble(Config.get(attribs.specie_name + "_init_usable_kcal"));

        this.animal = animal;
        this.attribs = attribs;
        this.movement = movement;
        this.interaction = interaction;
    }

    private boolean isRatioInNorm() {
        return thirst >= thirst_stable && hunger >= hunger_stable;
    }

    public void death() {
        is_dead = true;
    }

    // - - - - - - - - - //
    //  GOAL SEARCHING   //
    // - - - - - - - - - //

    protected Plant searchForPlant(final double range) {
        var goals = Simulation.searchForPlants(animal.getPosition(), range);
        goals.removeIf(plant -> !plant.is_edible);

        return animal.getClosestPlant(goals);
    }

    protected Animal searchForAnimalVictim(final double range) {
        var goals = Simulation.searchForAnimals(animal.getPosition(), range);
        goals.removeIf(animal -> animal.getPower() >= attribs.power);

        return animal.getClosestAnimal(goals);
    }

    protected Animal searchForAnimalReproduce(final double range) {
        final var animals = Simulation.searchForAnimals(animal.getPosition(), range);
        animals.removeIf(animal -> animal.getSex() == attribs.sex || !animal.getSpecieName().equals(attribs.specie_name));

        return animal.getClosestAnimal(animals);
    }

    /**
     * @todo not implemented yet
     */
    private Position searchForWater() {
        thirst = 100;
        return null;
    }

    protected Position searchForGoal(final utils.Needs goal, final double range) {
        if (goal == null) return null;

        switch (goal) {
            case HUNGER -> {
                if (attribs.is_herbivore) {
                    final var plant = searchForPlant(range);

                    if (plant == null) return null;
                    return searchForPlant(range).getPosition();
                }
                if (attribs.is_meat_eater) {
                    final var animal = searchForAnimalVictim(range);

                    if (animal == null) return null;
                    return searchForAnimalVictim(range).getPosition();
                }
            }
            case THIRST -> {
                return searchForWater();
            }
            case REPRODUCTION -> {
                final var animal = searchForAnimalReproduce(range);

                if (animal == null) return null;
                return searchForAnimalReproduce(range).getPosition();
            }
        }

        return null;
    }

    // = = = = = = = = = = //
    //    SET GOAL LOGIC   //
    // = = = = = = = = = = //

    /**
     * @apiNote Animals prioritize thirst and hunger over reproduction
     */
    public utils.Needs setMainGoal() {
        // 1. checks whether the main coefficients are considered normal
        if (isRatioInNorm() && reproduction <= 0) return utils.Needs.REPRODUCTION;

        // 2. check if in danger state of hunger/thirst
        if (thirst <= thirst_danger || hunger <= hunger_danger) {
            if (thirst <= hunger) return utils.Needs.THIRST;
            return utils.Needs.HUNGER;
        }

        // 3. choose smaller coefficient
        if (reproduction < hunger && reproduction < thirst && reproduction <= 0) return utils.Needs.REPRODUCTION;
        if (thirst <= hunger && thirst < thirst_stable) return utils.Needs.THIRST;
        if (hunger < hunger_stable) return utils.Needs.HUNGER;

        return null;
    }

    public void fillReproduction() {
        reproduction = 100;
        hunger -= Double.parseDouble(Config.get("animals_reproduction_cost_hunger"));
        // @todo: thirst -= Double.parseDouble(Config.get("animals_reproduction_cost_hunger"));
    }

    // = = = = = = = = = = //
    //     DRAIN LOGIC     //
    // = = = = = = = = = = //

    public void hungerDrain() {
        hunger -= 0.1;
    }

    public void thirstDrain() {
        thirst -= 0.2;
    }

    public void reproductionDrain() {
        reproduction -= 1;
    }

    // = = = = = = = = = = //
    //      EAT LOGIC      //
    // = = = = = = = = = = //

    /**
     * Kcal that other animals can get from eating this instance
     */
    public double getUsableKcal() {
        return usable_kcal;
    }

    public void fillHunger(final double kcal) {
        final double percentage_filled = (kcal * 100) / this.kcal;

        hunger += percentage_filled;
        if (hunger >= 100) hunger = 100;
    }

    public boolean shouldDie() {
        return hunger <= 0 || thirst <= 0;
    }
}
