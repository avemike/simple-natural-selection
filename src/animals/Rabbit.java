package animals;

import interfaces.Edible;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Rabbit extends Animal implements Edible {
    protected static BufferedImage species_image = null;

    protected Rabbit(final int x, final int y) throws Exception {
        super(x, y, Config.assets_path + "/" + "rabbit.png");
    }

    public static Rabbit create(final int x, final int y) {
        try {
            final var rabbit = new Rabbit(x, y);

            if (species_image == null)
                species_image = rabbit.loadImage();

            rabbit.setImage(species_image);
            return rabbit;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
