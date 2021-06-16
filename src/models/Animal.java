package models;

import Simulation.Simulation;
import services.Config;
import utils.Position;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 * @apiNote Drain rates are calculated based on
 */
public abstract class Animal extends GraphicalRepresentative {
    public boolean is_dead = false;
    protected double power;
    protected double health_points;
    protected double sight_range = 100; // @todo: calc it
    // Needs (expressed as a percentage - default is 90%)
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    protected double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));

    public Animal(final Simulation simulation, final int x, final int y, final String path, final int power) {
        super(simulation, x, y, Integer.parseInt(Config.get("animals_pixel_width")), Integer.parseInt(Config.get("animals_pixel_height")), path);
    }

    private boolean isRatioInNorm() {
        return thirst >= Double.parseDouble(Config.get("animals_stable_thirst")) && hunger >= Double.parseDouble(Config.get("animals_stable_hunger"));
    }

    protected void death() {
        is_dead = true;
    }

    protected Position searchForGoal(final String goal) {
        // todo: finish it
        return new Position(0, 0);
    }

    /**
     * @apiNote Animals prioritize thirst and hunger over reproduction
     */
    public String setMainGoal() {
        // 1. checks whether the main coefficients are considered normal
        if (isRatioInNorm()) return "reproduction";

        // 2. check if in danger state of hunger/thirst
        if (thirst <= thirst_danger || hunger <= hunger_danger) {
            if (thirst <= hunger) return "thirst";
            return "hunger";
        }

        // 3. choose smaller coefficient
        if (reproduction < hunger && reproduction < thirst) return "reproduction";
        if (thirst <= hunger) return "thirst";
        return "hunger";
    }

    /**
     * Method describing the behaviour of an individual in one iteration of the "event loop"
     */
    public void act() {
        if (is_dead) return;
        // 0. check if predator is in sight
        final Position nearby_predator = simulation.findNearbyPredator(coords, power, sight_range);

        if (nearby_predator != null) {
            // @todo: write
        }

        // 1. check status of needs
        if (hunger <= 0 || thirst <= 0) {
            death();
            return;
        }
        final var goal = setMainGoal();

        // 2. check whether the goal is in interaction range
        final var goal_position = searchForGoal(goal);

        // @todo: remove it
        this.setX(this.getX() + 0.5);

        // 3. check whether the goal is in sight

        // 4. search for the goal

        // 5. needs drain
    }

    // Getters
    public double getPower() {
        return power;
    }
}
