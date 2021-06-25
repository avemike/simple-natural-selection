package Simulation;

import models.Animal;
import models.Plant;

import java.util.Vector;

public final class InstancesContainer {
    public final static Vector<Animal> animals = new Vector<>();
    public final static Vector<Animal> animals_buffer = new Vector<>();
    public final static Vector<Plant> plants = new Vector<>();
    public final static Vector<Plant> plants_buffer = new Vector<>();

    private InstancesContainer() {
    }
}
