package utils;

import models.Animal;

public final class Reproduction {
    private final static double variety_diff = 0.3;

    private Reproduction() {
    }

    public static double newSize(final Animal animal_a, final Animal animal_b) {
        var size_a = animal_a.getSize();
        var size_b = animal_b.getSize();

        var random_modifier = 1 + Math.random() * variety_diff - variety_diff / 2;

        return (size_a + size_b) / 2 * random_modifier;
    }
}
