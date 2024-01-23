package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.AnimalsFactory;
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
    private boolean paused = false;
    private boolean stopped = false;
    private int dayOfSimulation = 0;
    private Statistics statistics;
    private final StatisticsCsvWriter statsWriter;

    public DarwinSimulation(Configuration config, String filePathToSaveStats) {
        statsWriter = new StatisticsCsvWriter(filePathToSaveStats);

        AnimalsFactory animalsFactory = new AnimalsFactory(config);

        worldMap = config.plantsGrowthVariantPoison() ?
                new PoisonedMap(config, animalsFactory) :
                new EquatorMap(config, animalsFactory);

        animals = generateAnimals(config, animalsFactory, worldMap);
    }

    private Set<Animal> generateAnimals(Configuration c, AnimalsFactory animalsFactory, WorldMap map) {
        Set<Animal> animalsSet = new HashSet<>(c.animalsStartNum());
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(c.mapWidth(), c.mapHeight(), c.animalsStartNum());

        for (Vector2d position : positionGenerator) {
            Animal animal = animalsFactory.createInitalAnimal(position);
            animalsSet.add(animal);
            map.place(animal);
        }

        return animalsSet;
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
                saveStatisticsToFile();

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

    private void saveStatisticsToFile() {
        statsWriter.saveToFile(dayOfSimulation, statistics);
    }

    private void mapChanged() {
        listeners.forEach(listener -> listener.mapChanged(worldMap));
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
    public List<Integer> getMostPopularGenes() {
        return statistics.getMostPopularGenes();
    }

    @Override
    public int getDayOfSimulation() {
        return dayOfSimulation;
    }
}
