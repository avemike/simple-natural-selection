package Simulation;

import services.Config;
import terrain.Terrain;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame implements Runnable {
    final Board board;

    public UI(final Terrain terrain) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Integer.parseInt(Config.get("gui_width")), Integer.parseInt(Config.get("gui_height")));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        board = new Board(terrain);

        add(board);
    }

    public void run() {
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(board);
    }
}
