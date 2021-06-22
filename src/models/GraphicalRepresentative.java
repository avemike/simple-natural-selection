package models;

import Simulation.Simulation;
import interfaces.Paintable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class GraphicalRepresentative extends Representative implements Paintable {
    protected static String path_to_image;
    protected BufferedImage representative_image;

    public GraphicalRepresentative(final Simulation simulation, final double x, final double y,
                                   final int size, final String path, final String specie_name) {
        super(simulation, x, y, size, size, specie_name);

        path_to_image = path;
    }

    protected BufferedImage loadImage() throws IOException {
        return ImageIO.read(new File(path_to_image));
    }

    protected void setImage(BufferedImage image) {
        representative_image = image;
    }

    public void paintComponent(Graphics2D graphics) {
        graphics.drawImage(representative_image, (int) coords.x - width / 2, (int) coords.y - height / 2, width, height, null);
    }
}
