package animals;

import interfaces.Edible;
import interfaces.MeatEater;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Fox extends Animal implements Edible, MeatEater {
    protected static BufferedImage species_image = null;

    protected Fox(final int x, final int y) {
        super(x, y, Config.assets_path + "/" + "fox.png", Integer.parseInt(Config.get("fox_init_power")));
    }

    public static Fox create(final int x, final int y) {
        try {
            final var fox = new Fox(x, y);

            if (species_image == null)
                species_image = fox.loadImage();

            fox.setImage(species_image);
            return fox;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
