package models;

import Simulation.Simulation;
import services.Config;
import utils.Needs;
import utils.Position;

import java.util.Vector;

import static utils.Position.getAngle;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 * @apiNote `Power` and `Drain rates` are calculated based on size
 */
public abstract class Animal extends GraphicalRepresentative {
    public boolean is_dead = false;
    protected boolean isHerbivore;
    protected boolean isMeateater;
    protected double power;
    protected double size;
    protected double speed;
    protected double sight_range;
    // Needs (expressed as a percentage - default is 90%)
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    protected double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));

    public Animal(final Simulation simulation, final int x, final int y, final String path, final double size) {
        super(simulation, x, y, Integer.parseInt(Config.get("animals_pixel_width")), Integer.parseInt(Config.get("animals_pixel_height")), path);

        // @todo: calc it
        this.size = size;
        power = size;
        speed = size * 0.01;
        // @todo: temp
    }

    private boolean isRatioInNorm() {
        return thirst >= Double.parseDouble(Config.get("animals_stable_thirst")) && hunger >= Double.parseDouble(Config.get("animals_stable_hunger"));
    }

    protected void death() {
        is_dead = true;
    }

    protected Position searchForGoal(final Needs goal) {
        Position goal_pos;
        switch (goal) {
            case HUNGER:
                var goals = isHerbivore
                        ? simulation.searchForPlants(coords, sight_range)
                        : simulation.searchForAnimals(coords, sight_range);


                runTo(getClosestInstance((Vector<Spatial>) goals).getPosition());
            case THIRST:
                // 0. search for water
            case REPRODUCTION:
                // 0. search for same specie with different sex
        }
        return new Position(0, 0);
    }

    protected Position calcNextStep(double angle) {
        if (angle > 360) angle -= 360;
        if (angle < 0) angle += 360;

        final var horizontal_diff = Math.cos(angle) * speed;
        final var vertical_diff = Math.sin(angle) * speed;

        return new Position(coords.x + horizontal_diff, coords.y + vertical_diff);
    }

    protected void runTo(final Position goal) {
        final var angle = getAngle(coords, goal);
        final var angleDiffs = new int[]{0, 45, -45, 90, -90, 135, -135, 180};

        Position next_pos = coords;

        // 0. check from which angle it does not collide with anything
        for (var angleDiff : angleDiffs) {
            next_pos = calcNextStep(angle + angleDiff);

            // 1. check if position collides
            boolean isColliding = simulation.checkIfCollides(next_pos);

            if (!isColliding) break;
        }

        // 2. move to position
        coords = next_pos;
    }

    /**
     * @apiNote Animals prioritize thirst and hunger over reproduction
     */
    public Needs setMainGoal() {
        // 1. checks whether the main coefficients are considered normal
        if (isRatioInNorm()) return Needs.REPRODUCTION;

        // 2. check if in danger state of hunger/thirst
        if (thirst <= thirst_danger || hunger <= hunger_danger) {
            if (thirst <= hunger) return Needs.THIRST;
            return Needs.HUNGER;
        }

        // 3. choose smaller coefficient
        if (reproduction < hunger && reproduction < thirst) return Needs.REPRODUCTION;
        if (thirst <= hunger) return Needs.THIRST;
        return Needs.HUNGER;
    }

    /**
     * Method describing the behaviour of an individual in one iteration of the "event loop"
     */
    public void act() {
        if (is_dead) return;

        // 0. check status of needs
        if (hunger <= 0 || thirst <= 0) {
            death();
            return;
        }

        // 1. check if predator is in sight
        final Position nearby_predator = simulation.findNearbyPredator(coords, power, sight_range);

        // OPTIONAL: run away
        if (nearby_predator != null) {
            runTo(Position.oppositePosition(coords, nearby_predator));
            return;
        }

        // 2. check whether the goal is in interaction range
        final var goal = setMainGoal();

        final var goal_position = searchForGoal(goal);

        // 3. check whether the goal is in sight

        // 4. search for the goal

        // 5. needs drain
    }

    // Getters
    public double getPower() {
        return power;
    }

    public double getSize() {
        return size;
    }
}
