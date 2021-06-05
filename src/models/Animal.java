package models;

import services.Config;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 */
public abstract class Animal extends GraphicalRepresentative {
    public boolean isDead = false;
    protected double power;
    protected double health_points;
    // Needs (expressed as a percentage - default is 90%)
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    protected double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    // Drain rate
    protected double hunger_drain = Double.parseDouble(Config.get("animals_initial_hunger_drain"));
    protected double thirst_drain = Double.parseDouble(Config.get("animals_initial_thirst_drain"));
    protected double reproduction_drain = Double.parseDouble(Config.get("animals_initial_reproduction_drain"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));

    public Animal(final int x, final int y, final String path, final int power) {
        super(x, y, Integer.parseInt(Config.get("animals_pixel_width")), Integer.parseInt(Config.get("animals_pixel_height")), path);
    }

    private boolean isRatioInNorm() {
        return thirst >= Double.parseDouble(Config.get("animals_stable_thirst")) & hunger >= Double.parseDouble(Config.get("animals_stable_hunger"));
    }

    private void death() {
        isDead = true;
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
     * @apiNote Method describing the behaviour of an individual in one iteration of the "event loop"
     */
    public void act() {
        if (isDead) return;
        // 0. check if predator is in sight

        // 1. check status of needs
        if (hunger <= 0 || thirst <= 0) {
            death();
            return;
        }
        String goal = setMainGoal();

        // 2. check whether the goal is in interaction range

        // 3. check whether the goal is in sight

        // 4. search for the goal

        // 5. needs drain

    }
}
