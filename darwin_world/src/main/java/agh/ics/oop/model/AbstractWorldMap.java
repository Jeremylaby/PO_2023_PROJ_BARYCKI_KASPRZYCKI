package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected void removePlant(Vector2d position){
        plants.remove(position);
    }
    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void mapChanged(String message) {
        observers.forEach(observer -> observer.mapChanged(this, message));
    }

    public void place(Animal animal) {
        if(!isanimal(animal.getPosition())){
            animals.put(animal.getPosition(),new HashSet<>());
        }
        animals.get(animal.getPosition()).add(animal);
    }

    public void move(Animal animal){
        animals.get(animal.getPosition()).remove(animal);
        if(animals.get(animal.getPosition()).isEmpty()){
            animals.remove(animal.getPosition());
        }
        animal.move(conf.mapWidth(), conf.mapHeight());
        place(animal);
        animals.get(animal.getPosition()).add(animal);
        mapChanged("Animal was moved");

    }

    public boolean isanimal(Vector2d position) {
        return animals.containsKey(position);
    }

    public Set<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }


    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Stream<Plant> stream=plants.values().stream().filter(plant -> !isanimal(plant.getPosition()));
        return Stream.concat(stream,animals.values().iterator().next().stream())
                .collect(Collectors.toMap(WorldElement::getPosition,worldElement -> worldElement));
    }
}
