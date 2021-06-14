package Simulation;

import animals.Fox;
import animals.Rabbit;
import models.Animal;
import services.Config;

import java.util.Vector;

public class Simulation {
    private final UI ui;
    private Vector<Animal> animals = new Vector<>();

    private Simulation() {
        ui = new UI(animals);
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals();

    }

    public void render() {

        ui.repaint();
    }

    private void initializeEventLoop() {

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
