package animals;

import Simulation.Simulation;
import interfaces.Edible;
import interfaces.MeatEater;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Fox extends Animal implements Edible, MeatEater {
    protected static BufferedImage species_image = null;

    protected Fox(final Simulation simulation, final double x, final double y, final double size) {
        super(simulation, x, y, Config.assets_path + "/" + "fox.png", size, "fox");

        final double specie_ratio = size / Double.parseDouble(Config.get("fox_init_size"));

        speed = specie_ratio * Double.parseDouble(Config.get("fox_init_speed"));
        interaction_range = 16 + size;
        sight_range = 1.25 * specie_ratio * Double.parseDouble(Config.get("fox_init_sight_range"));
        sex = Math.random() * 2 > 1;
        specie_name = "fox";
    }

    public static Fox create(final Simulation simulation, final double x, final double y, final double size) {
        try {
            final var fox = new Fox(simulation, x, y, size);

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
