package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomNumGenerator;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class EquatorMap extends AbstractWorldMap {
    private Map<Vector2d, Plant> Plants = new HashMap<>();
    private List<Vector2d> equator = new ArrayList<>();
    private List<Vector2d> wasteland =new ArrayList<>();
    private int equatorStart;
    private int equatorEnd;
    private Configuration conf;
    public EquatorMap(Configuration conf) {
        generateEquator(conf.mapHeight(),conf.mapWidth());
        generatePlants(conf.plantsStartNum());
    }

    private void generatePlants(int n) {//jak będziemy zjadać planta to będziemy go dodawać do equatora albo wasteland
        RandomNumGenerator equatororwasteland = new RandomNumGenerator(1,10);
        for(int i=0;i<n;i++){
            int m = equator.size();
            int k=wasteland.size();
            if(m==0&&k==0){
                return;
            }
            if(m>0 && equatororwasteland.generateRandomInt()<=8){
                Collections.shuffle(equator);
                Vector2d vector2d=equator.get(m-1);
                Plants.put(vector2d,new Plant(vector2d));
                equator.remove(m-1);
            }else{
                Collections.shuffle(wasteland);
                Vector2d vector2d=wasteland.get(k-1);
                Plants.put(vector2d,new Plant(vector2d));
                wasteland.remove(k-1);
            }

        }
    }

    private void generateEquator(int height, int width) {

        int numOfRows=Math.round((float) 0.2*height);
        if(numOfRows==0){
            numOfRows=1;
        }
        int n =  Math.round( (float) (height-numOfRows)/2);
        equatorStart=n;
        equatorEnd=n+numOfRows-1;
        for(int i=0;i<n;i++){
            for(int j=0;j<width;j++){
                wasteland.add(new Vector2d(j,i));
            }
        }
        for(int i=0;i<numOfRows;i++){
            for(int j=0;j<width;j++){
                equator.add(new Vector2d(j,n+i));
            }
        }
        for (int i=n+numOfRows;i<height;i++){
            for(int j=0;j<width;j++){
                wasteland.add(new Vector2d(j,i));
            }
        }

    }




    public Map<Vector2d, Plant> getGrasses() {
        return Map.copyOf(grasses);
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position)||grasses.get(position)!=null;
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement worldElement =super.objectAt(position);
        return worldElement!=null
                ?worldElement
                :grasses.get(position);

    }


    @Override
    public Boundary getCurrentBounds() {
        Vector2d minVector=new Vector2d(Integer.MAX_VALUE,Integer.MAX_VALUE);
        Vector2d maxVector=new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
        Map<Vector2d,WorldElement> worldElementMap=getElements();
        for(WorldElement worldElement:worldElementMap.values()){
            minVector=minVector.lowerLeft(worldElement.getPosition());
            maxVector=maxVector.upperRight(worldElement.getPosition());
        }
        return new Boundary(minVector,maxVector);
    }

    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Map<Vector2d,WorldElement> worldElementMap=super.getElements();
        for (Plant grass: grasses.values()){
            if(!worldElementMap.containsKey(grass.getPosition())) {
                worldElementMap.put(grass.getPosition(), grass);
            }
        }
        return worldElementMap;
    }

}
