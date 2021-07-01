package animals;

import interfaces.Edible;
import models.Animal;
import services.Config;
import simulation.Simulation;
import utils.Position;
import utils.Reproduction;

import java.awt.image.BufferedImage;

public class Fox extends Animal implements Edible {
    protected static BufferedImage species_image = null;

    protected Fox(final double x, final double y, final double size) {
        super(x, y, size, Config.assets_path + "/" + "fox.png", "fox");

        attribs.specie_size = Double.parseDouble(Config.get("fox_init_size"));
        final double specie_ratio = size / attribs.specie_size;

        attribs.speed = specie_ratio * Double.parseDouble(Config.get("fox_init_speed"));
        needs.kcal *= specie_ratio;
        attribs.interaction_range = 1.5 * size;
        attribs.sight_range = 1.25 * specie_ratio * Double.parseDouble(Config.get("fox_init_sight_range"));
        attribs.sex = Math.random() * 2 > 1;
        attribs.specie_name = "fox";
        attribs.is_meat_eater = true;
    }

    public static Fox create(final double x, final double y, final double size) {
        try {
            final var fox = new Fox(x, y, size);

            if (species_image == null)
                species_image = fox.loadImage();

            fox.setImage(species_image);
            return fox;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    public void reproduce(Animal animal) throws Exception {
        fillReproduction();
        animal.fillReproduction();

        double new_size = Reproduction.newSize(this, animal);

        final var child = Fox.create((coords.x + animal.getX()) / 2, (coords.y + animal.getY()) / 2, new_size);

        boolean isColliding = Simulation.checkIfCollides(child.getPosition(), new_size * 4, child);
        int range = 4;
        int angle = 0;
        Position next_pos = child.getPosition();

        while (isColliding) {
            if (angle == 360) {
                angle = 0;
                range += 4;

                if (range > 160) throw new Exception("No space for creating new animals!");
            }
            next_pos = child.calcNextStep(angle += 45, range);

            isColliding = Simulation.checkIfCollides(next_pos, new_size + 4, child);
        }

        child.setCoords(next_pos);
        Simulation.addAnimal(child);
    }
}
