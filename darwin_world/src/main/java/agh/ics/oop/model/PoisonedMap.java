package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoisonedMap extends AbstractWorldMap {
    private Boundary poisonedArea;
    private List<Vector2d> availablePositions = new ArrayList<>(    );

    public PoisonedMap(Configuration conf) {
        super(conf);
        generatePoisonedArea(conf.mapWidth(), conf.mapHeight());
        generatePlants(conf.plantsStartNum());
    }

    private void generatePlants(int n) {
        Collections.shuffle(availablePositions);
        for (int i=0;i<n;i++) {
            if(availablePositions.isEmpty()){
                return;
            }
            Vector2d plant=availablePositions.get(availablePositions.size()-1);
            availablePositions.remove(availablePositions.size()-1);
            if (plant.precedes(poisonedArea.rightUpper()) && plant.follows(poisonedArea.leftLower())) {
                plants.put(plant, new Plant(plant, true));
            } else {
                plants.put(plant, new Plant(plant));
            }
        }
    }

    private void generatePoisonedArea(int width, int height) {
        int a = (int) Math.max(Math.min(Math.round(Math.sqrt(width * height * 0.2)), Math.min(width, height)), 1);
        int x = RandomNumGenerator.generateRandomInt(0, width - a);
        int y = RandomNumGenerator.generateRandomInt(0, height - a);
        poisonedArea = new Boundary(new Vector2d(x, y), new Vector2d(x + a - 1, y + a - 1));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                availablePositions.add(new Vector2d(i, j));
            }
        }
    }
    @Override
    protected void feedAnimal(Animal animal){
        if(plants.get(animal.getPosition()).isPoisonous()){
            skillCheck(animal);
            if(plants.containsKey(animal.getPosition())){
                if(plants.get(animal.getPosition()).isPoisonous()){
                    animal.eat(-config.plantsEnergyValue());
                }else{
                    animal.eat(config.plantsEnergyValue());
                }
                removePlant(animal.getPosition());
            }
        }else{
            super.feedAnimal(animal);
            removePlant(animal.getPosition());
        }
    }

    private void skillCheck(Animal animal) {
        if(RandomNumGenerator.generateRandomInt(1,10)<=2){
            remove(animal);
            animal.dodge(getWidth()-1,getHeight()-1,RandomNumGenerator.generateRandomInt(0,7));
            place(animal);
        }
    }

    @Override
    protected void removePlant(Vector2d position) {
        availablePositions.add(position);
        super.removePlant(position);
    }
    
}
