package Simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import services.Config;
import terrain.Terrain;
import utils.FrameControler;

import java.util.Vector;

public class Simulation {
    private final UI ui;
    private final Terrain terrain = new Terrain();
    private Vector<Animal> animals = new Vector<>();

    private Simulation() {
        ui = new UI(animals, terrain);
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals();
        simulation.initializeEventLoop();
    }

    private void initializeEventLoop() {
        var condition = 100;
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

    private void initializeAnimals() {
        // 1. Add foxes
        for (var x = 0; x < Integer.parseInt(Config.get("animals_foxes_number")); x++)
            animals.add(Fox.create(this, x * 64, x + 24));
        // 2. Add rabbits
        for (var x = 0; x < Integer.parseInt(Config.get("animals_rabbits_number")); x++)
            animals.add(Rabbit.create(this, x * 32 % 500, 192 + 32 * (x * 32 / 500)));
    }
}
