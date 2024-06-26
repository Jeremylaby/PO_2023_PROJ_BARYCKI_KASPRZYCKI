package agh.ics.oop.model.map;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.AnimalsFactory;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.PreferredPosition;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.*;
import java.util.stream.Stream;

public class EquatorMap extends AbstractWorldMap {
    public static final double PLANT_ON_EQUATOR_PROBABILITY = 0.8;
    private List<Vector2d> availableEquatorPositions;
    private List<Vector2d> availableWastelandPositions;
    private int equatorStart;
    private int equatorEnd;

    public EquatorMap(Configuration config, AnimalsFactory animalsFactory) {
        super(config, animalsFactory);
        generateEquator(config.mapHeight(), config.mapWidth());
        generatePlants(config.plantsStartNum());
    }

    private void generateEquator(int height, int width) {
        int numOfRows = Math.max(Math.round((float) 0.2 * height), 1);
        equatorStart = Math.round((float) (height - numOfRows) / 2);
        equatorEnd = equatorStart + numOfRows - 1;

        availableEquatorPositions = Collections.synchronizedList(new ArrayList<>(numOfRows * width));
        availableWastelandPositions = new ArrayList<>((height - numOfRows) * width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y >= equatorStart && y <= equatorEnd) {
                    availableEquatorPositions.add(new Vector2d(x, y));
                } else {
                    availableWastelandPositions.add(new Vector2d(x, y));
                }
            }
        }
    }

    @Override
    protected void generatePlants(int numOfPlants) {
        for (int i = 0; i < numOfPlants; i++) {
            boolean plantOnEquator = Math.random() < PLANT_ON_EQUATOR_PROBABILITY;

            if (!availableEquatorPositions.isEmpty() && plantOnEquator) {
                addPlantFrom(availableEquatorPositions,true);
            } else if (!availableWastelandPositions.isEmpty()) {
                addPlantFrom(availableWastelandPositions,false);
            }
        }
    }

    private void addPlantFrom(List<Vector2d> availablePositions,boolean isEquator) {
        int lastIndex = availablePositions.size() - 1;
        int index = RandomNumGenerator.randomInt(0, lastIndex);

        Collections.swap(availablePositions, index, lastIndex);

        Vector2d position = availablePositions.get(lastIndex);
        plants.put(position, new Plant(position,false,isEquator));
        availablePositions.remove(lastIndex);
    }

    @Override
    protected void removePlant(Vector2d position) {
        if (position.y() >= equatorStart && position.y() <= equatorEnd) {
            availableEquatorPositions.add(position);
        } else {
            availableWastelandPositions.add(position);
        }
        super.removePlant(position);
    }

    @Override
    public Stream<PreferredPosition> getPreferredPlantPositions() {
        synchronized (availableEquatorPositions) {
            return availableEquatorPositions.stream().map(PreferredPosition::new);
        }
    }
}
