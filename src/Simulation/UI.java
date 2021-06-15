package Simulation;

import models.Animal;
import services.Config;
import terrain.Terrain;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class UI extends JFrame {
    private final Vector<Animal> animals;
    private final Terrain terrain;

    public UI(final Vector<Animal> animals, final Terrain terrain) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Integer.parseInt(Config.get("gui_width")), Integer.parseInt(Config.get("gui_height")));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // set paintable objects
        this.animals = animals;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
