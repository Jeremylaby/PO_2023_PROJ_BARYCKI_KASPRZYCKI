package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.map.EquatorMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation implements Runnable {
    public static final int SIMULATION_INTERVAL = 50;
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private final Set<Animal> animals;
    private final WorldMap worldMap;
    private boolean paused = false;
    private boolean stopped = false;
    private int dayOfSimulation = 0;

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    public Simulation(Configuration configuration) {
//        worldMap = switch (configuration.plantsGrowthVariantPoison()) {
//            case true: new PoisonousMap(configuration);
//            case false: new EquatorMap(configuration);
//        };
        worldMap = new EquatorMap(configuration);
        animals = new HashSet<>(configuration.animalsStartNum());

        generateAnimals(configuration);
    }

    private void generateAnimals(Configuration c) {
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(c.mapWidth(), c.mapHeight(), c.animalsStartNum());
        for (Vector2d position : positionGenerator) {
            Genome genome = new Genome(c.genomeSequenceVariantBackAndForth(), c.mutationsMinNum(), c.mutationsMaxNum(), c.genomeSize());
            Animal animal = new Animal(position, genome, c.animalsStartEnergy());
            animals.add(animal);
            worldMap.place(animal);
        }
    }

    public void run() {
        while (true) {
            if (stopped) return;

            if (!paused) {
                dayOfSimulation++;

                removeDeadAnimals();
                moveAnimals();
                feedAnimals();
                reproduceAnimals();
                growPlants();

                mapChanged();
            }

            try {
                Thread.sleep(SIMULATION_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeDeadAnimals() {
        List<Animal> deadAnimals = animals.stream()
                .filter(animal -> animal.getEnergy() <= 0)
                .toList();

        deadAnimals.forEach(animal -> {
            worldMap.removeDeadAnimal(animal,dayOfSimulation);
            animals.remove(animal);
        });;
    }

    private void moveAnimals() {
        animals.forEach(worldMap::move);
    }

    private void feedAnimals() {
        worldMap.feedAnimals();
    }

    private void reproduceAnimals() {
        animals.addAll(worldMap.reproduceAnimals());
    }

    private void growPlants() {
        worldMap.growPlants();
    }

    private void mapChanged() {
        listeners.forEach(listener -> listener.mapChanged(worldMap, ""));
    }

    public boolean toggle() {
        paused = !paused;
        return paused;
    }

    public void stop() {
        stopped = true;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}
