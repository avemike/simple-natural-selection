package models.animal;

import models.Animal;
import simulation.Simulation;
import utils.Position;

import static utils.Position.getAngle;

/**
 * @apiNote temporary_random_{direction, counter} is used in situations when animal does not have goal in sight
 * so instead of staying and doing nothing - waiting for goal to appear in sight, he simply moves in random
 * directions.
 * This direction is calculated once per `temporary_random_counter_base` turns
 */
public class Movement {
    final Attributes attribs;
    final Interaction interaction;
    final Needs needs;
    final Animal animal;
    protected double temporary_random_direction = 0;
    protected int temporary_random_counter = 0;
    protected int temporary_random_counter_base = 10;

    public Movement(final Animal animal, final Attributes attribs, final Needs needs, final Interaction interaction) {
        this.attribs = attribs;
        this.interaction = interaction;
        this.animal = animal;
        this.needs = needs;
    }

    protected void runToPosition(final Position goal) {
        runTo(goal, 0);
    }

    protected void runInDirection(final double angle) {
        runTo(null, angle);
    }

    private void runTo(final Position goal, double angle) {
        if (goal != null) angle = getAngle(animal.getPosition(), goal);
        final var angleDiffs = new int[]
                {0, 7, -7, 15, -15, 22, -22, 30, -30, 38, 45, -45, 60, -60,
                        90, -90, 160, -160, 180};

        Position next_pos = animal.getPosition();
        boolean isColliding = false;

        // 0. check from which angle it does not collide with anything
        for (var angleDiff : angleDiffs) {
            next_pos = animal.calcNextStep(angle + angleDiff, attribs.speed);

            // 1. check if position collides
            isColliding = Simulation.checkIfCollides(next_pos, 2, animal);

            if (!isColliding) break;
        }
        // 2. if everywhere were collision, get stuck
        if (isColliding) {
            return;
        }

        // 3. move to position
        animal.setCoords(next_pos);
    }

    void clearRandomDirection() {
        temporary_random_counter = 0;
    }

    void setRandomDirection() {
        temporary_random_counter = temporary_random_counter_base;
        temporary_random_direction = Math.random() * 360;
    }
}
