package agh.ics.oop.simulation;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.map.WorldMap;

public interface Simulation extends Runnable {
    void addListener(MapChangeListener listener);

    void removeListener(MapChangeListener listener);

    boolean toggle();

    void stop();

    WorldMap getWorldMap();

    Statistics getStatistics();

    int getDayOfSimulation();
}
