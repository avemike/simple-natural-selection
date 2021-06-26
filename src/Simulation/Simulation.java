package Simulation;

public class Simulation extends Game {
    private Simulation() {
        super();
    }

    public static void start() {
        final var simulation = new Simulation();

        simulation.initializeAnimals(simulation);
        simulation.initializePlants(simulation);

        simulation.initializeEventLoop();
    }
}
