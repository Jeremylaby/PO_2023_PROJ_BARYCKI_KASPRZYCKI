package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.AnimalFactory;
import agh.ics.oop.model.map.EquatorMap;
import agh.ics.oop.model.map.PoisonedMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class DarwinSimulation implements Simulation {
    private static final int SIMULATION_INTERVAL = 100;
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private final Set<Animal> animals;
    private final WorldMap worldMap;
    private final AnimalFactory animalFactory;
    private boolean paused = false;
    private boolean stopped = false;
    private int dayOfSimulation = 0;
    private Statistics statistics;
    private final String directoryToSaveFile;
    private final UUID id = UUID.randomUUID();

    public DarwinSimulation(Configuration config, String directoryToSave) {
        animals = new HashSet<>(config.animalsStartNum());
        animalFactory = new AnimalFactory(config);
        worldMap = config.plantsGrowthVariantPoison() ?
                new PoisonedMap(config, animalFactory) :
                new EquatorMap(config, animalFactory);
        directoryToSaveFile = directoryToSave;
        generateAnimals(config);
    }

    private void generateAnimals(Configuration c) {
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(c.mapWidth(), c.mapHeight(), c.animalsStartNum());

        for (Vector2d position : positionGenerator) {
            Animal animal = animalFactory.createInitalAnimal(position);
            animals.add(animal);
            worldMap.place(animal);
        }
    }

    @Override
    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
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

                generateStatistics();
//                statistics.saveToFile(id, dayOfSimulation,directoryToSaveFile);

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
            worldMap.removeDeadAnimal(animal, dayOfSimulation);
            animals.remove(animal);
        });
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

    private void generateStatistics() {
        this.statistics = new Statistics(worldMap);
    }

    private void mapChanged() {
        listeners.forEach(listener -> listener.mapChanged(worldMap, ""));
    }

    @Override
    public boolean toggle() {
        paused = !paused;
        return paused;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public WorldMap getWorldMap() {
        return worldMap;
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public int getDayOfSimulation() {
        return dayOfSimulation;
    }
}
