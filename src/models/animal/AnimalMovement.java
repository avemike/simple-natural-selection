package models.animal;

import Simulation.Simulation;
import utils.Position;

import java.util.logging.Level;

import static utils.Position.getAngle;

/**
 * @apiNote temporary_random_{direction, counter} is used in situations when animal does not have goal in sight
 * so instead of staying and doing nothing - waiting for goal to appear in sight, he simply moves in random
 * directions.
 * This direction is calculated once per `temporary_random_counter_base` turns
 */
public abstract class AnimalMovement extends AnimalNeeds {
    protected double temporary_random_direction = 0;
    protected int temporary_random_counter = 0;
    protected int temporary_random_counter_base = 10;

    public AnimalMovement(final Simulation simulation, final double x, final double y, final double size, final String path, final String specie_name) {
        super(simulation, x, y, (int) size, path, specie_name);
    }

    protected void runToPosition(final Position goal) {
        runTo(goal, 0);
    }

    protected void runInDirection(final double angle) {
        runTo(null, angle);
    }

    private void runTo(final Position goal, double angle) {
        if (goal != null) angle = getAngle(coords, goal);
        final var angleDiffs = new int[]
                {0, 7, -7, 15, -15, 22, -22, 30, -30, 38, 45, -45, 60, -60,
                        90, -90, 160, -160, 180};

        Position next_pos = coords;
        boolean isColliding = false;

        // 0. check from which angle it does not collide with anything
        for (var angleDiff : angleDiffs) {
            next_pos = calcNextStep(angle + angleDiff, speed);

            // 1. check if position collides
            isColliding = simulation.checkIfCollides(next_pos, 2, this);

            if (!isColliding) break;
        }
        // 2. if everywhere were collision, get stuck
        if (isColliding) {
            Simulation.log.log(Level.WARNING, "(" + coords.x + " " + coords.y + ") - Stuck [" + specie_name + "]");
            return;
        }

        // 3. move to position
        coords = next_pos;
    }

    private void clearRandomDirection() {
        temporary_random_counter = 0;
    }

    private void setRandomDirection() {
        temporary_random_counter = temporary_random_counter_base;
        temporary_random_direction = Math.random() * 360;
    }
}
