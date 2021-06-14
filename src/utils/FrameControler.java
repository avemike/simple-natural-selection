package utils;

import services.Config;

public final class FrameControler {
    private final static long delay = 1000 / Long.parseLong(Config.get("simulation_iterations_per_second"));

    private FrameControler() {
    }

    public static void run(long current_delay) {
        try {
            long delay_time = delay - current_delay;
            Thread.sleep(delay_time > 0 ? delay_time : 0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
