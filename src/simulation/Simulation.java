package simulation;

import models.Animal;
import models.Plant;
import terrain.Terrain;

import java.util.Vector;

public class Simulation extends Game {
    public final static Vector<Animal> animals = new Vector<>();
    public final static Vector<Animal> animals_buffer = new Vector<>();
    public final static Vector<Plant> plants = new Vector<>();
    public final static Vector<Plant> plants_buffer = new Vector<>();
    public final static Terrain terrain = new Terrain();
    private static Simulation simulation = null;

    public static void addAnimal(final Animal animal) {
        animals_buffer.add(animal);
    }

    public static void addPlant(final Plant plant) {
        plants_buffer.add(plant);
    }

    public static void start() {
        if (simulation == null) simulation = new Simulation();

        simulation.initializeAnimals();
        simulation.initializePlants();

        simulation.initializeEventLoop();
    }

    public static Simulation getInstance() {
        return simulation;
    }
}
