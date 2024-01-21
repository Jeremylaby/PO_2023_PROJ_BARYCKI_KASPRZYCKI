package agh.ics.oop.model.map;

import agh.ics.oop.model.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.AnimalFactory;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoisonedMap extends AbstractWorldMap {
    private final Boundary poisonedArea;
    private final List<Vector2d> availablePositions;

    public PoisonedMap(Configuration config, AnimalFactory animalFactory) {
        super(config, animalFactory);
        poisonedArea = createPoisonedArea(config.mapWidth(), config.mapHeight());
        availablePositions = new ArrayList<>(config.mapWidth()*config.mapHeight());
        generatePositions(config.mapWidth(), config.mapHeight());
        generatePlants(config.plantsStartNum());
    }

    private Boundary createPoisonedArea(int width, int height) {
        int a = (int) Math.max(Math.min(Math.round(Math.sqrt(width * height * 0.2)), Math.min(width, height)), 1);

        Vector2d lowerLeft = new Vector2d(
                RandomNumGenerator.randomInt(0, width - a),
                RandomNumGenerator.randomInt(0, height - a)
        );

        Vector2d upperRight = new Vector2d(
                lowerLeft.x() + a - 1,
                lowerLeft.y() + a - 1
        );

        return new Boundary(lowerLeft, upperRight);
    }

    private void generatePositions(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                availablePositions.add(new Vector2d(x, y));
            }
        }
    }

    @Override
    protected void generatePlants(int numOfPlants) {
        if (availablePositions.isEmpty()) return;

        for (int i = 0; i < numOfPlants; i++) {
            addPlant();
        }
    }

    private void addPlant() {
        int lastIndex = availablePositions.size() - 1;
        int index = RandomNumGenerator.randomInt(0, lastIndex);

        Collections.swap(availablePositions, index, lastIndex);

        Vector2d position = availablePositions.get(lastIndex);
        availablePositions.remove(lastIndex);

        if (position.precedes(poisonedArea.rightUpper()) && position.follows(poisonedArea.leftLower())) {
            plants.put(position, new Plant(position, true));
        } else {
            plants.put(position, new Plant(position));
        }
    }

    @Override
    protected void feedAnimal(Animal animal) {
        if (plants.get(animal.getPosition()).isPoisonous()) {
            skillCheck(animal);
            if (plants.containsKey(animal.getPosition())) {
                if (plants.get(animal.getPosition()).isPoisonous()) {
                    animal.eat(-config.plantsEnergyValue());
                } else {
                    animal.eat(config.plantsEnergyValue());
                }
            }
        } else {
            animal.eat(config.plantsEnergyValue());
        }
    }

    private void skillCheck(Animal animal) {
        if (RandomNumGenerator.randomInt(1, 10) <= 2) {
            remove(animal);
            animal.dodge(getWidth() - 1, getHeight() - 1, RandomNumGenerator.randomInt(0, 7));
            place(animal);
        }
    }

    @Override
    protected void removePlant(Vector2d position) {
        availablePositions.add(position);
        super.removePlant(position);
    }
}
