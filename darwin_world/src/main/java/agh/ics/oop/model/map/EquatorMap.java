package agh.ics.oop.model.map;

import agh.ics.oop.model.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.*;

public class EquatorMap extends AbstractWorldMap {
    public static final double PLANT_ON_EQUATOR_PROBABILITY = 0.8;
    private List<Vector2d> freeEquatorPositions;
    private List<Vector2d> freeWastelandPositions;
    private int equatorStart;
    private int equatorEnd;

    public EquatorMap(Configuration config) {
        super(config);
        generateEquator(config.mapHeight(), config.mapWidth());
        generatePlants(config.plantsStartNum());
    }

    @Override
    public void growPlants() {
        generatePlants(config.plantsNumPerDay());
    }

    private void generatePlants(int numOfPlants) {
        for (int i = 0; i < numOfPlants; i++) {
            boolean plantOnEquator = Math.random() < PLANT_ON_EQUATOR_PROBABILITY;

            if (!freeEquatorPositions.isEmpty() && plantOnEquator) {
                addPlantFrom(freeEquatorPositions);
            } else if (!freeWastelandPositions.isEmpty()) {
                addPlantFrom(freeWastelandPositions);
            }
        }
    }

    private void addPlantFrom(List<Vector2d> freePositions) {
        int lastIndex = freePositions.size() - 1;
        int index = RandomNumGenerator.randomInt(0, lastIndex);

        Collections.swap(freePositions, index, lastIndex);

        Vector2d position = freePositions.get(lastIndex);
        plants.put(position, new Plant(position));
        freePositions.remove(lastIndex);
    }

    private void generateEquator(int height, int width) {
        int numOfRows = Math.max(Math.round((float) 0.2 * height), 1);
        equatorStart = Math.round((float) (height - numOfRows) / 2);
        equatorEnd = equatorStart + numOfRows - 1;

        freeEquatorPositions = new ArrayList<>(numOfRows * width);
        freeWastelandPositions = new ArrayList<>((height - numOfRows) * width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y >= equatorStart && y <= equatorEnd) {
                    freeEquatorPositions.add(new Vector2d(x, y));
                } else {
                    freeWastelandPositions.add(new Vector2d(x, y));
                }
            }
        }
    }

    @Override
    protected void removePlant(Vector2d position) {
        if (position.y() >= equatorStart && position.y() <= equatorEnd) {
            freeEquatorPositions.add(position);
        } else {
            freeWastelandPositions.add(position);
        }
        super.removePlant(position);
    }
}
