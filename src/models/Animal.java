package models;

import Simulation.Simulation;
import services.Config;
import utils.Needs;
import utils.Position;

import static utils.Position.getAngle;


/**
 * @apiNote 100 hunger means fulfilled, 0 hunger is starvation
 * @apiNote `Power` and `Drain rates` are calculated based on size
 */
public abstract class Animal extends GraphicalRepresentative {
    public boolean is_dead = false;
    protected boolean sex;
    protected boolean is_herbivore;
    protected boolean is_meat_eater;
    protected double temporary_random_direction = 0;
    protected int temporary_random_counter = 0;
    protected double power;
    protected double size;
    protected double speed;
    protected double sight_range;
    protected double interaction_range;
    // Needs (expressed as a percentage - default is 90%)
    protected double hunger = Double.parseDouble(Config.get("animals_initial_hunger"));
    protected double thirst = Double.parseDouble(Config.get("animals_initial_thirst"));
    protected double reproduction = Double.parseDouble(Config.get("animals_initial_reproduction"));
    // Danger level
    protected double hunger_danger = Double.parseDouble(Config.get("animals_danger_hunger"));
    protected double thirst_danger = Double.parseDouble(Config.get("animals_danger_thirst"));

    public Animal(final Simulation simulation, final double x, final double y, final String path, final double size, final String specie_name) {
        super(simulation, x, y, Integer.parseInt(Config.get("animals_pixel_width")), Integer.parseInt(Config.get("animals_pixel_height")), path, specie_name);

        this.size = size;
        power = size;
        interaction_range = 16 + size;
    }

    private boolean isRatioInNorm() {
        return thirst >= Double.parseDouble(Config.get("animals_stable_thirst")) && hunger >= Double.parseDouble(Config.get("animals_stable_hunger"));
    }

    protected void death() {
        is_dead = true;
    }

    protected Position searchForGoal(final Needs goal, final double range) {
        Position goal_pos;
        // @todo: not implemented yet
        // 0. search for same specie with different sex
        switch (goal) {
            case HUNGER -> {
                if (is_herbivore) {
                    var goals = simulation.searchForPlants(coords, range);
                    goals.removeIf(plant -> !plant.isEdible);

                    var closest_plant = getClosestPlant(goals);

                    return closest_plant == null
                            ? null
                            : closest_plant.getPosition();
                }
                if (is_meat_eater) {
                    var goals = simulation.searchForAnimals(coords, range);
                    goals.removeIf(animal -> animal.power >= power);

                    var closest_animal = getClosestAnimal(goals);

                    return closest_animal == null
                            ? null
                            : closest_animal.getPosition();
                }
            }
            case THIRST -> {
                thirst = 100;
                return null;
            }
            case REPRODUCTION -> {
                final var animals = simulation.searchForAnimals(coords, range);
                animals.removeIf(animal -> animal.sex == sex || !animal.specie_name.equals(specie_name));

                var closest_animal = getClosestAnimal(animals);

                return closest_animal == null
                        ? null
                        : closest_animal.getPosition();
            }
        }

        // wont happen
        return null;
    }

    protected Position calcNextStep(double angle) {
        if (angle > 360) angle -= 360;
        if (angle < 0) angle += 360;

        final var horizontal_diff = Math.cos(angle) * speed;
        final var vertical_diff = Math.sin(angle) * speed;

        return new Position(coords.x + horizontal_diff, coords.y + vertical_diff);
    }

    protected void runTo(final Position goal, double angle) {
        if (goal != null) angle = getAngle(coords, goal);
        final var angleDiffs = new int[]{0, 45, -45, 90, -90, 135, -135, 180, 225, -225, 270, -270};

        Position next_pos = coords;
        boolean isColliding = false;

        // 0. check from which angle it does not collide with anything
        for (var angleDiff : angleDiffs) {
            next_pos = calcNextStep(angle + angleDiff);

            // 1. check if position collides
            isColliding = simulation.checkIfCollides(next_pos, this);

            if (!isColliding) break;
        }
        // 2. if everywhere were collision, get stuck
        if (isColliding) return;

        // 3. move to position
        coords = next_pos;
    }

    protected void runToPosition(final Position goal) {
        runTo(goal, 0);
    }

    protected void runInDirection(final double angle) {
        runTo(null, angle);
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

        // 0. needs drain
        // 1. check status of needs
        if (hunger <= 0 || thirst <= 0) {
            death();

            clearRandomDirection();
            return;
        }

        // 2. check if predator is in sight
        final Position nearby_predator = simulation.findNearbyPredator(coords, power, sight_range);

        // OPTIONAL: run away
        if (nearby_predator != null) {
            runToPosition(Position.oppositePosition(coords, nearby_predator));

            clearRandomDirection();
            return;
        }

        // 3. check whether the goal is in interaction range
        final var goal = setMainGoal();

        var goal_position = searchForGoal(goal, interaction_range);

        if (goal_position != null) {
            // 2. optional interact with the goal

            clearRandomDirection();
            return;
        }

        // 4. check whether the goal is in sight
        goal_position = searchForGoal(goal, sight_range);

        if (goal_position != null) {
            runToPosition(goal_position);

            clearRandomDirection();
            return;
        }
        // 5. search for the goal - move into random direction
        if (temporary_random_counter > 0) temporary_random_counter--;
        else setRandomDirection();

        runInDirection(temporary_random_direction);
    }

    private void clearRandomDirection() {
        temporary_random_counter = 0;
    }

    private void setRandomDirection() {
        temporary_random_counter = 10;
        temporary_random_direction = Math.random() * 360;
    }

    // Getters
    public double getPower() {
        return power;
    }

    public double getSize() {
        return size;
    }
}
