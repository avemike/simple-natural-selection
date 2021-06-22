package Simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import models.Plant;
import plants.Shrub;
import plants.Tree;
import services.Config;
import terrain.Terrain;
import utils.FrameControler;
import utils.Position;

import java.util.Vector;

import static utils.Position.isInRange;

public class Simulation {
    private final UI ui;
    private final Terrain terrain = new Terrain();
    private final Vector<Animal> animals = new Vector<>();
    private final Vector<Plant> plants = new Vector<>();

    private Simulation() {
        ui = new UI(animals, plants, terrain);
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals();
        simulation.initializePlants();

        simulation.initializeEventLoop();
    }

    // Interaction methods: animals - environment/other animals

    /**
     * @apiNote if there isn't nearby predator within range, return null
     * @apiNote algorithm returns first animal that fullfills the condition
     * @todo search for the nearest predator
     */
    public Position findNearbyPredator(final Position animal_pos, final double power, final double range) {
        for (var animal : animals) {
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

        for (var plant : plants) {
            boolean isInRange = isInRange(src, plant.getPosition(), range);

            if (isInRange) found_plants.add(plant);
        }

        return found_plants;
    }

    public Vector<Animal> searchForAnimals(final Position src, final double range) {
        var found_animals = new Vector<Animal>();

        for (var animal : animals) {
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

    public boolean checkIfCollides(final Position pos) {
        // 0. check collisions with instances
        for (var animal : animals) {
            boolean isColliding = isInRange(animal.getPosition(), pos, animal.getSize());

            if (isColliding) return true;
        }
        // @todo: vegetation

        // 1. check collision with borders
        if (pos.x < 0 || pos.y < 0) return true;
        if (pos.x > terrain.getWidth() || pos.y > terrain.getHeight()) return true;

        // 2. check collision with water
        if (terrain.isCollidingWithWater(pos)) return true;

        // 3. otherwise return false
        return false;

    }

    private void initializeEventLoop() {
        var condition = 10000;
        long iteration_start_time;

        while (condition-- >= 0) {
            // 0. set timer
            iteration_start_time = System.currentTimeMillis();

            // 1. run animals
            for (var animal : animals) {
                if (animal != null)
                    animal.act();
            }
            // 2. "garbage collector"
            animals.removeIf(animal -> animal.is_dead);

            // 3. paint
            ui.repaint();

            // 4. delay
            FrameControler.run(System.currentTimeMillis() - iteration_start_time);

        }
    }

    private Position generateNonCollidingPos() {
        Position random_position;
        int max_counter = 40;
        do {
            random_position = new Position(Math.random() * terrain.getWidth(), Math.random() * terrain.getHeight());
        } while (checkIfCollides(random_position) && max_counter-- > 0);

        return random_position;
    }

    private void initializeAnimals() {
        // 1. Add foxes
        for (int x = 0, max = Integer.parseInt(Config.get("animals_foxes_number")); x < max; x++) {
            var random_position = generateNonCollidingPos();

            animals.add(Fox.create(this, random_position.x, random_position.y));
        }
        // 2. Add rabbits
        for (int x = 0, max = Integer.parseInt(Config.get("animals_rabbits_number")); x < max; x++) {
            var random_position = generateNonCollidingPos();

            animals.add(Rabbit.create(this, random_position.x, random_position.y));
        }

    }

    private void initializePlants() {
        // 1. Add trees
        for (int x = 0, max = Integer.parseInt(Config.get("plants_trees_number")); x < max; x++) {
            var random_position = generateNonCollidingPos();

            plants.add(Tree.create(this, random_position.x, random_position.y));
        }
        // 2. Add shrubs
        for (int x = 0, max = Integer.parseInt(Config.get("plants_shrubs_number")); x < max; x++) {
            var random_position = generateNonCollidingPos();

            plants.add(Shrub.create(this, random_position.x, random_position.y));
        }
    }

}
