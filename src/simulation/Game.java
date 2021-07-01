package simulation;

import animals.Fox;
import animals.Rabbit;
import plants.Shrub;
import plants.Tree;
import services.Config;
import ui.UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class Game extends WorldInteraction implements ActionListener {
    public final static Logger log = Logger.getLogger("simulation");
    private final UI ui;

    private final int edible_plants_num = Integer.parseInt(Config.get("plants_shrubs_number"));
    private int event_loop_condition = 100000;

    protected Game() {
        ui = new UI(Simulation.terrain);

        javax.swing.SwingUtilities.invokeLater(ui);
    }

    private void changeLoopCondition() {
        event_loop_condition--;
    }

    private boolean hasLoopEnded() {
        return event_loop_condition <= 0;
    }

    private void loopStep() throws Exception {
        if (hasLoopEnded()) return;

        // 1. run animals
        for (var animal : Simulation.animals) {
            if (animal != null)
                animal.act();
        }
        // 2. "garbage collector"
        Simulation.animals.removeIf(animal -> animal.isDead());
        Simulation.plants.removeIf(plant -> plant.is_dead);

        // 3. create missing edible plants
        final var current_edible_plants_num = Simulation.plants.stream()
                .filter(plant -> plant.is_edible)
                .count();

        initializeMissingEdiblePlants((int) (edible_plants_num - current_edible_plants_num));

        // 4. add instances from buffers (newly created)
        Simulation.animals.addAll(Simulation.animals_buffer);
        Simulation.plants.addAll(Simulation.plants_buffer);

        // 5. clean buffers
        Simulation.animals_buffer.clear();
        Simulation.plants_buffer.clear();


        // 6. paint
        ui.repaint();

        // 7. change iteration condition
        changeLoopCondition();
    }

    protected void initializeEventLoop() {
        final var timer = new Timer(1000 / Integer.parseInt(Config.get("simulation_iterations_per_second")), this);

        timer.start();
    }

    protected void initializeAnimals() {
        // 1. Add foxes
        for (int x = 0, max = Integer.parseInt(Config.get("animals_foxes_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("fox_init_size"));

            var random_position = generateNonCollidingPos(size);

            Simulation.animals.add(Fox.create(random_position.x, random_position.y, size));
        }
        // 2. Add rabbits
        for (int x = 0, max = Integer.parseInt(Config.get("animals_rabbits_number")); x < max; x++) {
            var randomness_factor = 0.85 + Math.random() * 0.3;
            var size = randomness_factor * Double.parseDouble(Config.get("rabbit_init_size"));

            var random_position = generateNonCollidingPos(size);

            Simulation.animals.add(Rabbit.create(random_position.x, random_position.y, size));
        }
    }

    protected void initializePlants() {
        // 1. Add trees
        for (int x = 0, max = Integer.parseInt(Config.get("plants_trees_number")); x < max; x++) {
            var random_position = generateNonCollidingPos(0);

            Simulation.plants.add(Tree.create(random_position.x, random_position.y));
        }
        // 2. Add shrubs
        for (int x = 0; x < Integer.parseInt(Config.get("plants_shrubs_number")); x++) {
            var random_position = generateNonCollidingPos(0);

            Simulation.plants.add(Shrub.create(random_position.x, random_position.y));
        }
    }

    protected void initializeMissingEdiblePlants(final int n) {
        for (int i = 0; i < n; i++) {
            var random_position = generateNonCollidingPos(0);

            Simulation.addPlant(Shrub.create(random_position.x, random_position.y));
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
