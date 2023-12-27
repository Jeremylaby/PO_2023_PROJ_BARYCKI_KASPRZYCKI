package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private List<Simulation> simulations = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {

        this.simulations = simulations;
        simulations.forEach(simulation -> threads.add(new Thread(simulation)));
    }

    public void runSync() {
        simulations.forEach(simulation -> simulation.run());
    }

    public void runAsync() throws InterruptedException{
        threads.forEach(thread -> thread.start());
    }

    private void awaitSimulationsEnd() throws InterruptedException {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

    }
        public void runAsyncInThreadPool() throws InterruptedException{
            threads.forEach(thread -> executorService.submit(thread));
            awaitSimulationsEnd();
        }
}
