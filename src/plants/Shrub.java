package plants;

import models.Plant;
import services.Config;

import java.awt.image.BufferedImage;

public class Shrub extends Plant {
    protected static BufferedImage species_image = null;

    protected Shrub(final double x, final double y) {
        super(x, y, Config.assets_path + "/" + "shrub.png", "shrub");

        kcal = Double.parseDouble(Config.get("plants_shrub_kcal"));
        is_edible = true;
    }

    public static Shrub create(final double x, final double y) {
        try {
            final var shrub = new Shrub(x, y);

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
