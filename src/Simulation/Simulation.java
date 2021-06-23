package Simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import models.Plant;
import plants.Shrub;
import plants.Tree;
import services.Config;
import terrain.Terrain;
import utils.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import static utils.Position.isInRange;

public class Simulation implements ActionListener {
    private final UI ui;
    private final Terrain terrain = new Terrain();
    private final Vector<Animal> animals = new Vector<>();
    private final Vector<Animal> animals_buffer = new Vector<>();
    private final Vector<Plant> plants = new Vector<>();
    private final Vector<Plant> plants_buffer = new Vector<>();

    private int event_loop_condition = 100000;

    private Simulation() {
        ui = new UI(animals, plants, terrain);

        javax.swing.SwingUtilities.invokeLater(ui);
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals();
        simulation.initializePlants();

        simulation.initializeEventLoop();
    }

    private void changeLoopCondition() {
        event_loop_condition--;
    }

    private boolean hasLoopEnded() {
        return event_loop_condition <= 0;
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

    public void addAnimal(final Animal animal) {
        animals_buffer.add(animal);
    }

    public void addPlant(final Plant plant) {
        plants_buffer.add(plant);
    }

    // @todo:
    //  public Vector<Animal> searchForVegetation(final Position src, final double range) {
    //  }
    public boolean checkIfCollides(final Position pos, final double range) {
        // 0. check collisions with instances
        for (var animal : animals) {
            if (animal.getPosition() != pos) continue;
            boolean isColliding = isInRange(pos, animal.getPosition(), 4 + animal.getSize() + range);

            if (isColliding) return true;
        }
        for (var animal : animals_buffer) {
            if (animal.getPosition() != pos) continue;
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

    private void loopStep() throws Exception {
        if (hasLoopEnded()) return;

        // 1. run animals
        for (var animal : animals) {
            if (animal != null)
                animal.act();
        }
        // 2. "garbage collector"
        animals.removeIf(animal -> animal.is_dead);

        // 3. add instances from buffers (newly created)
        if (!animals_buffer.isEmpty()) {
            System.out.println("I ShITTED MYSELF");
        }
        animals.addAll(animals_buffer);
        plants.addAll(plants_buffer);

        animals_buffer.clear();
        plants_buffer.clear();

        // 4. paint
        ui.repaint();

        // 5. change iteration condition
        changeLoopCondition();
    }

    private void initializeEventLoop() {
        final var timer = new Timer(1000 / Integer.parseInt(Config.get("simulation_iterations_per_second")), this);

        timer.start();
    }

    private Position generateNonCollidingPos(final double size) {
        Position random_position;
        int max_counter = 40;
        do {
            random_position = new Position(Math.random() * terrain.getWidth(), Math.random() * terrain.getHeight());
        } while (checkIfCollides(random_position, size) && max_counter-- > 0);

        return random_position;
    }

    private void initializeAnimals() {
        // 1. Add foxes
        for (int x = 0, max = Integer.parseInt(Config.get("animals_foxes_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("fox_init_size"));

            var random_position = generateNonCollidingPos(size);

            animals.add(Fox.create(this, random_position.x, random_position.y, size));
        }
        // 2. Add rabbits
        for (int x = 0, max = Integer.parseInt(Config.get("animals_rabbits_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("rabbit_init_size"));

            var random_position = generateNonCollidingPos(size);

            animals.add(Rabbit.create(this, random_position.x, random_position.y, size));
        }
    }

    private void initializePlants() {
        // 1. Add trees
        for (int x = 0, max = Integer.parseInt(Config.get("plants_trees_number")); x < max; x++) {
            var random_position = generateNonCollidingPos(0);

            plants.add(Tree.create(this, random_position.x, random_position.y));
        }
        // 2. Add shrubs
        for (int x = 0, max = Integer.parseInt(Config.get("plants_shrubs_number")); x < max; x++) {
            var random_position = generateNonCollidingPos(0);

            plants.add(Shrub.create(this, random_position.x, random_position.y));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            loopStep();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
