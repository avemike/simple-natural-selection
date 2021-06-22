package Simulation;

import models.Animal;
import models.Plant;
import services.Config;
import terrain.Terrain;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class UI extends JFrame implements Runnable {
    final Board board;

    public UI(final Vector<Animal> animals, final Vector<Plant> plants, final Terrain terrain) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Integer.parseInt(Config.get("gui_width")), Integer.parseInt(Config.get("gui_height")));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        board = new Board(animals, plants, terrain);

        add(board);
    }

    public void run() {
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(board);
    }
}
