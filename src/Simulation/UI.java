package Simulation;

import models.Animal;
import models.Plant;
import services.Config;
import terrain.Terrain;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class UI extends JFrame {
    private final Vector<Animal> animals;
    private final Vector<Plant> plants;
    private final Terrain terrain;

    public UI(final Vector<Animal> animals, final Vector<Plant> plants, final Terrain terrain) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Integer.parseInt(Config.get("gui_width")), Integer.parseInt(Config.get("gui_height")));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // set paintable objects
        this.animals = animals;
        this.plants = plants;
        this.terrain = terrain;
    }

    public void paint(Graphics g) {
        var g2D = (Graphics2D) g;

        try {
            // 0. Draw terrain
            if (terrain == null) throw new Exception("Terrain is empty");
            terrain.paint(g2D);

            // 1. Draw all animals
            if (animals == null) return;

            for (var animal : animals) {
                if (animal != null)
                    animal.paint(g2D);
            }

            // 2. Draw all plants
            if (plants == null) return;

            for (var plant : plants) {
                if (plant != null)
                    plant.paint(g2D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
