package models;

import services.Config;

public class Animal extends GraphicalRepresentation {
    public Animal(final int x, final int y, final String path) throws Exception {
        super(x, y, Integer.parseInt(Config.get("animals_pixel_width")), Integer.parseInt(Config.get("animals_pixel_height")), path);
    }
}
