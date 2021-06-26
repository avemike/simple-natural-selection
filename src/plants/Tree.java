package plants;

import models.Plant;
import services.Config;

import java.awt.image.BufferedImage;

public class Tree extends Plant {
    protected static BufferedImage species_image = null;
    public final boolean is_edible = false;

    protected Tree(final double x, final double y) {
        super(x, y, Config.assets_path + "/" + "tree.png", "tree");
    }

    public static Tree create(final double x, final double y) {
        try {
            final var tree = new Tree(x, y);

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
