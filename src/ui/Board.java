package ui;

import terrain.Terrain;
import utils.InstancesContainer;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final Terrain terrain;

    public Board(final Terrain terrain) {
        // set paintable objects
        this.terrain = terrain;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2D = (Graphics2D) g;

        try {
            // 0. Draw terrain
            if (terrain == null) throw new Exception("Terrain is empty");
            terrain.paintComponent(g2D);

            // 1. Draw all animals
            for (var animal : InstancesContainer.animals) {
                if (animal != null)
                    animal.paintComponent(g2D);
            }

            // 2. Draw all plants
            for (var plant : InstancesContainer.plants) {
                if (plant != null)
                    plant.paintComponent(g2D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
