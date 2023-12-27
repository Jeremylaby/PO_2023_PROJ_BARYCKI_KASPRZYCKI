package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int operationCounter = 0;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {//Wiem że w zadaniu była podana innakolejność ale tak jest dla mnie bardziej czytelna
        operationCounter++;
        System.out.println("Map id: " + worldMap.getId());
        System.out.println("Update nr " + operationCounter + ":");
        System.out.println(message);
        System.out.println(worldMap.toString());
    }
}
