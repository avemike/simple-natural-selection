package simulation;

public class Simulation extends Game {
    private Simulation() {
        super();
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals();
        simulation.initializePlants();

        simulation.initializeEventLoop();
    }
}
