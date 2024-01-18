package agh.ics.oop.model.map;

import agh.ics.oop.model.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashMap;
import java.util.Map;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Plant> grasses = new HashMap<>();
    public GrassField(Configuration config) {
        super(config);
        generateGrass(config.plantsStartNum());
    }

    private void generateGrass(int grassCount) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(config.mapWidth(), config.mapHeight(), grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Plant(grassPosition, false));
        }
    }


    public Map<Vector2d, Plant> getGrasses() {
        return Map.copyOf(grasses);
    }

    @Override
    public void growPlants() {

    }

//    @Override
//    public boolean isOccupied(Vector2d position) {
//        return super.isOccupied(position)||grasses.get(position)!=null;
//    }


//    @Override
//    public WorldElement objectAt(Vector2d position) {
//        WorldElement worldElement =super.objectAt(position);
//        return worldElement!=null
//                ?worldElement
//                :grasses.get(position);
//
//    }


//    public Boundary getCurrentBounds() {
//        Vector2d minVector=new Vector2d(Integer.MAX_VALUE,Integer.MAX_VALUE);
//        Vector2d maxVector=new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
//        Map<Vector2d,WorldElement> worldElementMap=getElements();
//        for(WorldElement worldElement:worldElementMap.values()){
//            minVector=minVector.lowerLeft(worldElement.getPosition());
//            maxVector=maxVector.upperRight(worldElement.getPosition());
//        }
//        return new Boundary(minVector,maxVector);
//    }

//    @Override
//    public Map<Vector2d, WorldElement> getElements() {
//        Map<Vector2d,WorldElement> worldElementMap=super.getElements();
//        for (Plant grass: grasses.values()){
//            if(!worldElementMap.containsKey(grass.getPosition())) {
//                worldElementMap.put(grass.getPosition(), grass);
//            }
//        }
//        return worldElementMap;
//    }
}
