package Simulation;

import animals.Fox;
import animals.Rabbit;
import plants.Shrub;
import plants.Tree;
import services.Config;
import ui.UI;
import utils.InstancesContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class Game extends WorldInteraction implements ActionListener {
    public final static Logger log = Logger.getLogger("Simulation");
    private final UI ui;
    private int event_loop_condition = 100000;

    protected Game() {
        ui = new UI(terrain);

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
        for (var animal : InstancesContainer.animals) {
            if (animal != null)
                animal.act();
        }
        // 2. "garbage collector"
        InstancesContainer.animals.removeIf(animal -> animal.is_dead);

        // 3. add instances from buffers (newly created)
        InstancesContainer.animals.addAll(InstancesContainer.animals_buffer);
        InstancesContainer.plants.addAll(InstancesContainer.plants_buffer);

        InstancesContainer.animals_buffer.clear();
        InstancesContainer.plants_buffer.clear();

        // 4. paint
        ui.repaint();

        // 5. change iteration condition
        changeLoopCondition();
    }

    protected void initializeEventLoop() {
        final var timer = new Timer(1000 / Integer.parseInt(Config.get("simulation_iterations_per_second")), this);

        timer.start();
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

    protected void initializePlants(final Simulation simulation) {
        // 1. Add trees
        for (int x = 0, max = Integer.parseInt(Config.get("plants_trees_number")); x < max; x++) {
            var random_position = generateNonCollidingPos(0);

            InstancesContainer.plants.add(Tree.create(simulation, random_position.x, random_position.y));
        }
        // 2. Add shrubs
        for (int x = 0, max = Integer.parseInt(Config.get("plants_shrubs_number")); x < max; x++) {
            var random_position = generateNonCollidingPos(0);

            InstancesContainer.plants.add(Shrub.create(simulation, random_position.x, random_position.y));
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
