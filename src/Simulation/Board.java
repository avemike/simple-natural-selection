package Simulation;

import models.Animal;
import models.Plant;
import terrain.Terrain;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Board extends JPanel {
    private final Vector<Animal> animals;
    private final Vector<Plant> plants;
    private final Terrain terrain;

    public Board(final Vector<Animal> animals, final Vector<Plant> plants, final Terrain terrain) {
        // set paintable objects
        this.animals = animals;
        this.plants = plants;
        this.terrain = terrain;
    }

    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        var g2D = (Graphics2D) g;

        try {
            // 0. Draw terrain
            if (terrain == null) throw new Exception("Terrain is empty");
            terrain.paintComponent(g2D);

            // 1. Draw all animals
            if (animals == null) return;

            for (var animal : animals) {
                if (animal != null)
                    animal.paintComponent(g2D);
            }

            // 2. Draw all plants
            if (plants == null) return;

            for (var plant : plants) {
                if (plant != null)
                    plant.paintComponent(g2D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
