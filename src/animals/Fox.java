package animals;

import Simulation.Simulation;
import interfaces.Edible;
import interfaces.MeatEater;
import models.Animal;
import services.Config;

import java.awt.image.BufferedImage;

public class Fox extends Animal implements Edible, MeatEater {
    protected static BufferedImage species_image = null;

    protected Fox(final Simulation simulation, final double x, final double y) {
        super(simulation, x, y, Config.assets_path + "/" + "fox.png", Integer.parseInt(Config.get("fox_init_size")));

        speed = 3;
        interaction_range = 60;
        sight_range = 120;
        sex = Math.random() * 2 > 1;
        specie_name = "fox";
    }

    public static Fox create(final Simulation simulation, final double x, final double y) {
        try {
            final var fox = new Fox(simulation, x, y);

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
