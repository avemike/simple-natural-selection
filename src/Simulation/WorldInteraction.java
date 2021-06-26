package Simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import models.Plant;
import models.Representative;
import services.Config;
import terrain.Terrain;
import utils.InstancesContainer;
import utils.Position;

import java.util.Vector;

import static utils.Position.isInRange;

public class WorldInteraction {
    protected final Terrain terrain = new Terrain();

    /**
     * @apiNote if there isn't nearby predator within range, return null
     * @apiNote algorithm returns first animal that fullfills the condition
     * @todo search for the nearest predator
     */
    public Position findNearbyPredator(final Position animal_pos, final double power, final double range) {
        for (var animal : InstancesContainer.animals) {
            // 0. check whether it is a threat
            if (animal.getPower() <= power) continue;

            // 1. check if it is within given range
            if (!Position.isInRange(animal_pos, animal.getPosition(), range)) continue;

            // 2. return position
            return animal.getPosition();
        }
        // OPTIONAL: 3. otherwise return null
        return null;
    }

    public Vector<Plant> searchForPlants(final Position src, final double range) {
        var found_plants = new Vector<Plant>();

        for (var plant : InstancesContainer.plants) {
            boolean isInRange = isInRange(src, plant.getPosition(), range);

            if (isInRange) found_plants.add(plant);
        }

        return found_plants;
    }

    public Vector<Animal> searchForAnimals(final Position src, final double range) {
        var found_animals = new Vector<Animal>();

        for (var animal : InstancesContainer.animals) {
            boolean isInRange = isInRange(src, animal.getPosition(), range);

            if (isInRange && animal.getPosition() != src) found_animals.add(animal);
        }

        return found_animals;
    }

    public Position searchForWater(final Position src, final double range) throws Exception {
        throw new Exception("Not implemented yet");
    }

    // @todo:
    //  public Vector<Animal> searchForVegetation(final Position src, final double range) {
    //  }
    public boolean checkIfCollides(final Position pos, final double range, final Representative original) {
        // 0. check collisions with instances
        for (var animal : InstancesContainer.animals) {
            if (animal == original) continue;
            boolean isColliding = isInRange(pos, animal.getPosition(), 4 + animal.getSize() + range);

            if (isColliding) return true;
        }
        for (var animal : InstancesContainer.animals_buffer) {
            if (animal == original) continue;
            boolean isColliding = isInRange(pos, animal.getPosition(), 4 + animal.getSize() + range);

            if (isColliding) return true;
        }
        // @todo: vegetation

        // 1. check collision with borders
        if (pos.x < 16 || pos.y < 16) return true;
        if (pos.x > terrain.getWidth() - 16 || pos.y > terrain.getHeight() - 16) return true;

        // 2. check collision with water
        if (terrain.isCollidingWithWater(pos, range)) return true;

        // 3. otherwise return false
        return false;

    }

    protected Position generateNonCollidingPos(final double size) {
        Position random_position;
        int max_counter = 40;
        do {
            random_position = new Position(Math.random() * terrain.getWidth(), Math.random() * terrain.getHeight());
        } while (checkIfCollides(random_position, size, null) && max_counter-- > 0);

        return random_position;
    }

    protected void initializeAnimals(final Simulation simulation) {
        // 1. Add foxes
        for (int x = 0, max = Integer.parseInt(Config.get("animals_foxes_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("fox_init_size"));

            var random_position = generateNonCollidingPos(size);

            InstancesContainer.animals.add(Fox.create(simulation, random_position.x, random_position.y, size));
        }
        // 2. Add rabbits
        for (int x = 0, max = Integer.parseInt(Config.get("animals_rabbits_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("rabbit_init_size"));

            var random_position = generateNonCollidingPos(size);

            InstancesContainer.animals.add(Rabbit.create(simulation, random_position.x, random_position.y, size));
        }
    }
}
