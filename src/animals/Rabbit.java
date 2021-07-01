package animals;

import models.Animal;
import services.Config;
import services.InstancesContainer;
import simulation.Simulation;
import utils.Position;
import utils.Reproduction;

import java.awt.image.BufferedImage;

public class Rabbit extends Animal {
    protected static BufferedImage species_image = null;

    protected Rabbit(final double x, final double y, final double size) {
        super(x, y, Integer.parseInt(Config.get("rabbit_init_size")), Config.assets_path + "/" + "rabbit.png", "rabbit");

        final double specie_ratio = size / Double.parseDouble(Config.get("rabbit_init_size"));

        attribs.speed = specie_ratio * Double.parseDouble(Config.get("rabbit_init_speed"));
        attribs.sight_range = 1.2 * specie_ratio * Double.parseDouble(Config.get("rabbit_init_sight_range"));
        attribs.interaction_range = 1.5 * size;
        attribs.sex = Math.random() * 2 > 1;
        attribs.specie_name = "rabbit";
        attribs.is_herbivore = true;
    }

    public static Rabbit create(final double x, final double y, final double size) {
        try {
            final var rabbit = new Rabbit(x, y, size);

            if (species_image == null)
                species_image = rabbit.loadImage();

            rabbit.setImage(species_image);
            return rabbit;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    public void reproduce(Animal animal) {
        fillReproduction();
        animal.fillReproduction();

        double new_size = Reproduction.newSize(this, animal);

        final var child = Rabbit.create((coords.x + animal.getX()) / 2, (coords.y + animal.getY()) / 2, new_size);

        assert child != null;
        boolean isColliding = Simulation.checkIfCollides(child.getPosition(), new_size + 4, child);
        int range = 1;
        int angle = 0;
        Position next_pos = child.getPosition();

        while (isColliding) {
            if (angle == 360) {
                angle = 0;
                range += 2;

                if (range > 5 * attribs.size) return;
            }
            next_pos = child.calcNextStep(angle += 30, range);

            isColliding = Simulation.checkIfCollides(next_pos, new_size + 2, child);
        }

        child.setCoords(next_pos);
        InstancesContainer.addAnimal(child);
    }
}
