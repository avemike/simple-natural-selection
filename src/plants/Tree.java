package plants;

import Simulation.Simulation;
import models.Plant;
import services.Config;

import java.awt.image.BufferedImage;

public class Tree extends Plant {
    protected static BufferedImage species_image = null;

    protected Tree(final Simulation simulation, final double x, final double y) {
        super(simulation, x, y, Config.assets_path + "/" + "tree.png");
    }

    public static Tree create(final Simulation simulation, final double x, final double y) {
        try {
            final var tree = new Tree(simulation, x, y);

            if (species_image == null)
                species_image = tree.loadImage();

            tree.setImage(species_image);
            return tree;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

}
