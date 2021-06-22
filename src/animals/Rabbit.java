package animals;

import Simulation.Simulation;
import interfaces.Edible;
import interfaces.Herbivore;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Rabbit extends Animal implements Edible, Herbivore {
    protected static BufferedImage species_image = null;

    protected Rabbit(final Simulation simulation, final double x, final double y, final double size) {
        super(simulation, x, y, Config.assets_path + "/" + "rabbit.png", Integer.parseInt(Config.get("rabbit_init_size")), "rabbit");

        final double specie_ratio = size / Double.parseDouble(Config.get("rabbit_init_size"));

        speed = specie_ratio * Double.parseDouble(Config.get("rabbit_init_speed"));
        sight_range = 1.2 * specie_ratio * Double.parseDouble(Config.get("rabbit_init_sight_range"));
        sex = Math.random() * 2 > 1;
        specie_name = "rabbit";
    }

    public static Rabbit create(final Simulation simulation, final double x, final double y, final double size) {
        try {
            final var rabbit = new Rabbit(simulation, x, y, size);

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
