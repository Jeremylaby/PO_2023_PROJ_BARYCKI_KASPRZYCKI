package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.*;

public class EquatorMap extends AbstractWorldMap {
    private final List<Vector2d> equator = new ArrayList<>();
    private final List<Vector2d> wasteland = new ArrayList<>();
    private int equatorStart;
    private int equatorEnd;

    public EquatorMap(Configuration config) {
        super(config);
        generateEquator(config.mapHeight(), config.mapWidth());
        generatePlants(config.plantsStartNum());
    }

    private void generatePlants(int n) {//jak będziemy zjadać planta to będziemy go dodawać do equatora albo wasteland
        Collections.shuffle(equator);
        Collections.shuffle(wasteland);
        for (int i = 0; i < n; i++) {
            int m = equator.size();
            int k = wasteland.size();
            if (m == 0 && k == 0) {
                return;
            }
            if (m > 0 && RandomNumGenerator.generateRandomInt(1, 10) <= 8) {
                Vector2d vector2d = equator.get(m - 1);
                plants.put(vector2d, new Plant(vector2d));
                equator.remove(m - 1);
            } else {
                Vector2d vector2d = wasteland.get(k - 1);
                plants.put(vector2d, new Plant(vector2d));
                wasteland.remove(k - 1);
            }

        }
    }

    private void generateEquator(int height, int width) {
        int numOfRows = Math.round((float) 0.2 * height);
        if (numOfRows == 0) {
            numOfRows = 1;
        }
        int n = Math.round((float) (height - numOfRows) / 2);
        equatorStart = n;
        equatorEnd = n + numOfRows - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < width; j++) {
                wasteland.add(new Vector2d(j, i));
            }
        }
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < width; j++) {
                equator.add(new Vector2d(j, n + i));
            }
        }
        for (int i = n + numOfRows; i < height; i++) {
            for (int j = 0; j < width; j++) {
                wasteland.add(new Vector2d(j, i));
            }
        }

    }
    @Override
    protected void removePlant(Vector2d position) {
        if (position.y() >= equatorStart && position.y() <= equatorEnd) {
            equator.add(position);
        } else {
            wasteland.add(position);
        }
        super.removePlant(position);
    }

}
