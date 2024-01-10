package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractWorldMap implements WorldMap {
    private final UUID id = UUID.randomUUID();
    protected Map<Vector2d, Set<Animal>> animals= new HashMap<>();
    protected Map<Vector2d,Plant> plants=new HashMap<>();
    private List<MapChangeListener> observers = new ArrayList<>();
    protected Configuration conf;

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void mapChanged(String message) {
        observers.forEach(observer -> observer.mapChanged(this, message));
    }

    public void place(Animal animal) {
        animals.get(animal.getPosition()).add(animal);
        mapChanged("Animal was placed on: " + animal.getPosition().toString());
    }

    public void move(Animal animal){
        animals.get(animal.getPosition()).remove(animal);
        if(animals.get(animal.getPosition()).isEmpty()){
            animals.remove(animal.getPosition());
        }
        animal.move(conf.mapWidth(), conf.mapHeight());
        if (!animals.containsKey(animal.getPosition())){
            animals.put(animal.getPosition(),new HashSet<>());
        }
        animals.get(animal.getPosition()).add(animal);
        mapChanged("Animal was moved");

    }

    public boolean isanimal(Vector2d position) {

        return animals.containsKey(position);
    }

    public Collection<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }


    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Map<Vector2d, WorldElement> worldElementMap = new HashMap<>();//todo
        return worldElementMap;
    }
}
