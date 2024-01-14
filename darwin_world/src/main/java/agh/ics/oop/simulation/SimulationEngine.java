package agh.ics.oop.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void runSimulation(Simulation simulation) {
        executorService.submit(simulation);
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
