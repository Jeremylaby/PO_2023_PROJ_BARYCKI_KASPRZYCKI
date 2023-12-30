package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractWorldMap implements WorldMap {
    private final UUID id = UUID.randomUUID();
    protected Multimap<Vector2d, Animal> animals = ArrayListMultimap.create();
    protected Map<Vector2d,Plant> plants=new HashMap<>();
    private List<MapChangeListener> observers = new ArrayList<>();

    protected int nutrition;
    protected int height;
    protected int width;
    protected Boundary boundary=new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    private void mapChanged(String message) {
        observers.forEach(observer -> observer.mapChanged(this, message));
    }

    public void place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            mapChanged("Animal was placed on: " + animal.getPosition().toString());
        }
    }

    public synchronized void move(Animal animal) {
        if (this.animalsAt(animal.getPosition()).contains(animal)) {
            Vector2d oldPosition = animal.getPosition();
            MapDirection oldOrientation = animal.getOrientation();

            animals.remove(animal.getPosition(), animal);
            animal.move(getCurrentBounds());
            animals.put(animal.getPosition(), animal);

            if (!oldPosition.equals(animal.getPosition())) {
                mapChanged("Animal was moved from " + oldPosition + " to " + animal.getPosition().toString());
            } else if (oldOrientation != animal.getOrientation()) {
                mapChanged("Animal " + animal.getPosition() + " rotated from " + oldOrientation + " to " + animal.getOrientation());
            }
        }
    }

    public boolean canMoveTo(Vector2d position) {

        return !animals.containsKey(position);
    }

    public Collection<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }


    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Map<Vector2d, WorldElement> worldElementMap = new HashMap<>();
        for (Animal animal : animals.values()) {
            worldElementMap.put(animal.getPosition(), animal);
        }
        return worldElementMap;
    }

    @Override
    public abstract Boundary getCurrentBounds();
}
