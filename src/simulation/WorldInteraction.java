package simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import models.Plant;
import models.Representative;
import services.Config;
import services.InstancesContainer;
import utils.Position;

import java.util.Vector;

import static utils.Position.isInRange;

public class WorldInteraction {
    public static Vector<Plant> searchForPlants(final Position src, final double range) {
        var found_plants = new Vector<Plant>();

        for (var plant : InstancesContainer.plants) {
            boolean isInRange = isInRange(src, plant.getPosition(), range);

            if (isInRange) found_plants.add(plant);
        }

        return found_plants;
    }

    /**
     * @apiNote if there isn't nearby predator within range, return null
     * @apiNote algorithm returns first animal that fullfills the condition
     * @todo search for the nearest predator
     */
    public static Position findNearbyPredator(final Position animal_pos, final double power, final double range) {
        for (var animal : InstancesContainer.animals) {
            // 0. check whether it is a threat
            if (animal.attribs.getPower() <= power) continue;

            // 1. check if it is within given range
            if (!Position.isInRange(animal_pos, animal.getPosition(), range)) continue;

            // 2. return position
            return animal.getPosition();
        }
        // OPTIONAL: 3. otherwise return null
        return null;
    }

    public static Vector<Animal> searchForAnimals(final Position src, final double range) {
        var found_animals = new Vector<Animal>();

        for (var animal : InstancesContainer.animals) {
            boolean isInRange = isInRange(src, animal.getPosition(), range);

            if (isInRange && animal.getPosition() != src) found_animals.add(animal);
        }

        return found_animals;
    }

    public static Position searchForWater(final Position src, final double range) throws Exception {
        throw new Exception("Not implemented yet");
    }

    // @todo:
    //  public Vector<Animal> searchForVegetation(final Position src, final double range) {
    //  }
    public static boolean checkIfCollides(final Position pos, final double range, final Representative original) {
        // 0. check collisions with instances
        for (var animal : InstancesContainer.animals) {
            if (animal == original) continue;
            boolean isColliding = isInRange(pos, animal.getPosition(), 2 + animal.attribs.getSize() + range);

            if (isColliding) return true;
        }
        for (var animal : InstancesContainer.animals_buffer) {
            if (animal == original) continue;
            boolean isColliding = isInRange(pos, animal.getPosition(), 2 + animal.attribs.getSize() + range);

            if (isColliding) return true;
        }
        for (var plant : InstancesContainer.plants) {
            if (plant == original) continue;
            boolean isColliding = isInRange(pos, plant.getPosition(), 2 + plant.getSize() + range);

            if (isColliding) return true;
        }
        for (var plant : InstancesContainer.plants_buffer) {
            if (plant == original) continue;
            boolean isColliding = isInRange(pos, plant.getPosition(), 2 + plant.getSize() + range);

            if (isColliding) return true;
        }

        // 1. check collision with borders
        if (pos.x < 16 || pos.y < 16) return true;
        if (pos.x > InstancesContainer.terrain.getWidth() - 16 || pos.y > InstancesContainer.terrain.getHeight() - 16)
            return true;

        // 2. check collision with water
        if (InstancesContainer.terrain.isCollidingWithWater(pos, range)) return true;

        // 3. otherwise return false
        return false;

    }

    protected static Position generateNonCollidingPos(final double size) {
        Position random_position;
        int max_counter = 40;
        do {
            random_position = new Position(Math.random() * InstancesContainer.terrain.getWidth(),
                    Math.random() * InstancesContainer.terrain.getHeight());
        } while (checkIfCollides(random_position, size, null) && max_counter-- > 0);

        return random_position;
    }

    protected void initializeAnimals(final Simulation simulation) {
        // 1. Add foxes
        for (int x = 0, max = Integer.parseInt(Config.get("animals_foxes_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("fox_init_size"));

            var random_position = generateNonCollidingPos(size);

            InstancesContainer.animals.add(Fox.create(random_position.x, random_position.y, size));
        }
        // 2. Add rabbits
        for (int x = 0, max = Integer.parseInt(Config.get("animals_rabbits_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("rabbit_init_size"));

            var random_position = generateNonCollidingPos(size);

            InstancesContainer.animals.add(Rabbit.create(random_position.x, random_position.y, size));
        }
    }
}
