package services;

import models.Animal;
import models.Plant;
import terrain.Terrain;

import java.util.Vector;

public final class InstancesContainer {
    public final static Vector<Animal> animals = new Vector<>();
    public final static Vector<Animal> animals_buffer = new Vector<>();
    public final static Vector<Plant> plants = new Vector<>();
    public final static Vector<Plant> plants_buffer = new Vector<>();
    public final static Terrain terrain = new Terrain();

    private InstancesContainer() {
    }

    public static void addAnimal(final Animal animal) {
        InstancesContainer.animals_buffer.add(animal);
    }

    public static void addPlant(final Plant plant) {
        InstancesContainer.plants_buffer.add(plant);
    }
}
