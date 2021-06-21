package animals;

import Simulation.Simulation;
import interfaces.Edible;
import interfaces.Herbivore;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Rabbit extends Animal implements Edible, Herbivore {
    protected static BufferedImage species_image = null;

    protected Rabbit(final Simulation simulation, final double x, final double y) {
        super(simulation, x, y, Config.assets_path + "/" + "rabbit.png", Integer.parseInt(Config.get("rabbit_init_power")));

        speed = 2.4;
        interaction_range = 30;
        sight_range = 100;
        sex = Math.random() * 2 > 1;
    }

    public static Rabbit create(final Simulation simulation, final double x, final double y) {
        try {
            final var rabbit = new Rabbit(simulation, x, y);

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
