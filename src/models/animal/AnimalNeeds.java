package models.animal;

import models.Animal;
import models.Plant;
import services.Config;
import simulation.Simulation;
import utils.Needs;
import utils.Position;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 * @apiNote `Power` and `Drain rates` are calculated based on size
 * @apiNote hunger is filled by dividing ate usable eaten instance kcal by overall kcal
 */
public abstract class AnimalNeeds extends AnimalAttributes {
    protected final double usable_kcal;
    public boolean is_dead = false;
    protected double kcal;
    // Needs expressed as percentage
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    protected double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));

    public AnimalNeeds(final double x, final double y, final double size,
                       final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);

        kcal = Double.parseDouble(Config.get(specie_name + "_init_kcal"));
        usable_kcal = Double.parseDouble(Config.get(specie_name + "_init_usable_kcal"));
    }

    private boolean isRatioInNorm() {
        return thirst >= Double.parseDouble(Config.get("animals_stable_thirst")) && hunger >= Double.parseDouble(Config.get("animals_stable_hunger"));
    }

    public void death() {
        is_dead = true;
    }

    // - - - - - - - - - //
    //  GOAL SEARCHING   //
    // - - - - - - - - - //

    protected Plant searchForPlant(final double range) {
        var goals = Simulation.searchForPlants(coords, range);
        goals.removeIf(plant -> !plant.isEdible);

        return getClosestPlant(goals);
    }

    protected Animal searchForAnimalVictim(final double range) {
        var goals = Simulation.searchForAnimals(coords, range);
        goals.removeIf(animal -> animal.power >= power);

        return getClosestAnimal(goals);
    }

    protected Animal searchForAnimalReproduce(final double range) {
        final var animals = Simulation.searchForAnimals(coords, range);
        animals.removeIf(animal -> animal.sex == sex || !animal.specie_name.equals(specie_name));

        return getClosestAnimal(animals);
    }

    /**
     * @todo not implemented yet
     */
    private Position searchForWater() {
        thirst = 100;
        return null;
    }

    protected Position searchForGoal(final Needs goal, final double range) {
        switch (goal) {
            case HUNGER -> {
                if (is_herbivore) {
                    final var plant = searchForPlant(range);

                    if (plant == null) return null;
                    return searchForPlant(range).getPosition();
                }
                if (is_meat_eater) {
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
    public Needs setMainGoal() {
        // 1. checks whether the main coefficients are considered normal
        if (isRatioInNorm() && reproduction <= 0) return Needs.REPRODUCTION;

        // 2. check if in danger state of hunger/thirst
        if (thirst <= thirst_danger || hunger <= hunger_danger) {
            if (thirst <= hunger) return Needs.THIRST;
            return Needs.HUNGER;
        }

        // 3. choose smaller coefficient
        if (reproduction < hunger && reproduction < thirst && reproduction <= 0) return Needs.REPRODUCTION;
        if (thirst <= hunger) return Needs.THIRST;
        return Needs.HUNGER;
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
