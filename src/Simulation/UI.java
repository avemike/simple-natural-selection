package Simulation;

import models.Animal;
import services.Config;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class UI extends JFrame {
    private final Vector<Animal> animals;

    public UI(final Vector<Animal> animals) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Integer.parseInt(Config.get("gui_width")), Integer.parseInt(Config.get("gui_height")));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.animals = animals;
    }

    public void paint(Graphics g) {
        var g2D = (Graphics2D) g;

        try {
            // 1. Draw all animals
            for (var animal : animals) {
                if (animal != null)
                    animal.paint(g2D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
