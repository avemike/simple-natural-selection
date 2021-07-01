package models.animal;

import models.Animal;
import models.Plant;
import simulation.Simulation;
import utils.Needs;
import utils.Position;

import java.util.logging.Level;


public class Interaction {
    final Attributes attribs;
    final Movement movement;
    final models.animal.Needs needs;
    final Animal animal;

    public Interaction(final Animal animal, final Attributes attribs, final models.animal.Needs needs, final Movement movement) {
        this.animal = animal;
        this.attribs = attribs;
        this.needs = needs;
        this.movement = movement;
    }

    /**
     * Method describing the behaviour of an individual in one iteration of the "event loop"
     */
    public void act() throws Exception {
        if (needs.is_dead) return;

        // 0. needs drain
        needs.hungerDrain();
        needs.reproductionDrain();

        // 1. check status of needs
        if (needs.shouldDie()) {
            needs.death();

            return;
        }

        // 2. check if predator is in sight
        final Position nearby_predator = Simulation.findNearbyPredator(animal, attribs.power, attribs.sight_range);

        // 2.1 OPTIONAL: run away
        if (nearby_predator != null) {
            movement.runToPosition(Position.oppositePosition(animal.getPosition(), nearby_predator));

            movement.clearRandomDirection();
            return;
        }

        // 3. check whether the goal is in interaction range
        final var goal = needs.setMainGoal();

        boolean doesInteracted = searchForGoalAndInteract(goal);

        if (doesInteracted) {
            movement.clearRandomDirection();
            return;
        }

        // 4. check whether the goal is in sight
        var goal_position = needs.searchForGoal(goal, attribs.sight_range);

        if (goal_position != null) {
            movement.runToPosition(goal_position);

            movement.clearRandomDirection();
            return;
        }
        // 5. search for the goal - move into random direction
        if (movement.temporary_random_counter > 0) movement.temporary_random_counter--;
        else movement.setRandomDirection();

        movement.runInDirection(movement.temporary_random_direction);
    }

    protected void eatPlant(final Plant plant) {
        needs.fillHunger(plant.getKcal());
        plant.death();
    }

    protected void eatAnimal(final Animal animal) {
        Simulation.log.log(Level.INFO, "(" + attribs.specie_name + ") ate (" + animal.getSpecieName() + ")");
        needs.fillHunger(animal.getUsableKcal());
        animal.death();
    }

    protected boolean searchForGoalAndInteract(Needs goal) throws Exception {
        if (goal == null) return false;

        switch (goal) {
            case HUNGER -> {
                if (attribs.is_herbivore) {
                    var goals = Simulation.searchForPlants(animal.getPosition(), attribs.interaction_range);
                    goals.removeIf(plant -> !plant.is_edible);

                    var closest_plant = animal.getClosestPlant(goals);

                    if (closest_plant != null)
                        eatPlant(closest_plant);

                    return closest_plant != null;
                }
                if (attribs.is_meat_eater) {
                    var goals = Simulation.searchForAnimals(animal.getPosition(), attribs.interaction_range);
                    goals.removeIf(animal -> animal.getPower() >= attribs.power);

                    var closest_animal = animal.getClosestAnimal(goals);

                    if (closest_animal != null)
                        eatAnimal(closest_animal);

                    return closest_animal != null;
                }
            }
            case THIRST -> {
                // @todo: implement
                needs.thirst = 100;
                return true;
            }
            case REPRODUCTION -> {
                final var animals = Simulation.searchForAnimals(animal.getPosition(), attribs.interaction_range);
                animals.removeIf(animal -> animal.getSex() == attribs.sex || !animal.getSpecieName().equals(attribs.specie_name));

                var closest_animal = animal.getClosestAnimal(animals);

                if (closest_animal != null)
                    animal.reproduce(closest_animal);

                return closest_animal != null;
            }
        }
        return false;
    }
}
