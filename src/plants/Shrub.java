package plants;

import Simulation.Simulation;
import interfaces.Edible;
import models.Plant;
import services.Config;

import java.awt.image.BufferedImage;

public class Shrub extends Plant implements Edible {
    protected static BufferedImage species_image = null;

    protected Shrub(final Simulation simulation, final int x, final int y) {
        super(simulation, x, y, Config.assets_path + "/" + "shrub.png");

        kcal = Double.parseDouble(Config.get("plants_shrub_kcal"));
    }

    public static Shrub create(final Simulation simulation, final int x, final int y) {
        try {
            final var shrub = new Shrub(simulation, x, y);

            if (species_image == null)
                species_image = shrub.loadImage();

            shrub.setImage(species_image);
            return shrub;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

}
