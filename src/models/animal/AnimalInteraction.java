package models.animal;

import models.Animal;
import models.Plant;
import org.jetbrains.annotations.NotNull;
import simulation.Simulation;
import utils.Needs;
import utils.Position;


public abstract class AnimalInteraction extends AnimalMovement {
    public AnimalInteraction(final double x, final double y, final double size, final String path, final String specie_name) {
        super(x, y, (int) size, path, specie_name);
    }

    /**
     * Method describing the behaviour of an individual in one iteration of the "event loop"
     */
    public void act() throws Exception {
        if (is_dead) return;

        // 0. needs drain

        hungerDrain();
        reproductionDrain();

        // 1. check status of needs
        if (shouldDie()) {
            death();

            return;
        }

        // 2. check if predator is in sight
        final Position nearby_predator = Simulation.findNearbyPredator(coords, power, sight_range);

        // 2.1 OPTIONAL: run away
        if (nearby_predator != null) {
            runToPosition(Position.oppositePosition(coords, nearby_predator));

            clearRandomDirection();
            return;
        }

        // 3. check whether the goal is in interaction range
        final var goal = setMainGoal();

        boolean doesInteracted = searchForGoalAndInteract(goal);

        if (doesInteracted) {
            clearRandomDirection();
            return;
        }

        // 4. check whether the goal is in sight
        var goal_position = searchForGoal(goal, sight_range);

        if (goal_position != null) {
            runToPosition(goal_position);

            clearRandomDirection();
            return;
        }
        // 5. search for the goal - move into random direction
        if (temporary_random_counter > 0) temporary_random_counter--;
        else setRandomDirection();

        runInDirection(temporary_random_direction);
    }

    protected void eatPlant(final @NotNull Plant plant) {
        fillHunger(plant.getKcal());
        plant.death();
    }

    protected void eatAnimal(final @NotNull Animal animal) {
        fillHunger(animal.getUsableKcal());
        animal.death();
    }

    protected boolean searchForGoalAndInteract(Needs goal) throws Exception {
        switch (goal) {
            case HUNGER -> {
                if (is_herbivore) {
                    var goals = Simulation.searchForPlants(coords, interaction_range);
                    goals.removeIf(plant -> !plant.is_edible);

                    var closest_plant = getClosestPlant(goals);

                    if (closest_plant != null)
                        eatPlant(closest_plant);

                    return closest_plant != null;
                }
                if (is_meat_eater) {
                    var goals = Simulation.searchForAnimals(coords, interaction_range);
                    goals.removeIf(animal -> animal.power >= power);

                    var closest_animal = getClosestAnimal(goals);

                    if (closest_animal != null)
                        eatAnimal(closest_animal);

                    return closest_animal != null;
                }
            }
            case THIRST -> {
                // @todo: implement
                thirst = 100;
                return true;
            }
            case REPRODUCTION -> {
                final var animals = Simulation.searchForAnimals(coords, interaction_range);
                animals.removeIf(animal -> animal.sex == sex || !animal.specie_name.equals(specie_name));

                var closest_animal = getClosestAnimal(animals);

                if (closest_animal != null) {
                    // @todo: expand reproduce logic

                    reproduce(closest_animal);
                }

                return closest_animal != null;
            }
        }
        return false;
    }

    protected abstract void reproduce(Animal animal) throws Exception;

    private void clearRandomDirection() {
        temporary_random_counter = 0;
    }

    private void setRandomDirection() {
        temporary_random_counter = 10;
        temporary_random_direction = Math.random() * 360;
    }

    // Getters
    public double getPower() {
        return power;
    }

    public double getSize() {
        return size;
    }
}
