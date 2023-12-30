package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomNumGenerator;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class EquatorMap extends AbstractWorldMap {
    private Map<Vector2d, Plant> plants = new HashMap<>();
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
        Collections.shuffle(equator);
        Collections.shuffle(wasteland);
        for(int i=0;i<n;i++){
            int m = equator.size();
            int k = wasteland.size();
            if(m==0&&k==0){
                return;
            }
            if(m>0 && equatororwasteland.generateRandomInt()<=8){;
                Vector2d vector2d=equator.get(m-1);
                plants.put(vector2d,new Plant(vector2d));
                equator.remove(m-1);
            }else{
                Vector2d vector2d=wasteland.get(k-1);
                plants.put(vector2d,new Plant(vector2d));
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
    public Map<Vector2d, Plant> getPlants() {
        return Map.copyOf(plants);
    }
    public void move(Animal animal){
        Vector2d oldposition=animal.getPosition();
        MapDirection oldorientation=animal.getOrientation();
        animalsMap.remove(animal.getPosition(),animal);
        animal.move(conf.mapWidth(), conf.mapHeight());
        animalsMap.put(animal.getPosition(),animal);
        if(oldposition!=animal.getPosition()){
            mapChanged("Animal rotated from: "+oldorientation+" to: "+animal.getOrientation()
            " and noved from : "+oldposition+" to: "+animal.getPosition());
        }else{
            mapChanged("Animal bounced new orientation: "+animal.getOrientation());
        }
    }
    private Animal findStrongest(List<Animal> animals){
        List<Animal> candidates = new ArrayList<>();
        Animal strongestAnimal = null;
        for(Animal animal:animals){
            if(candidates.size()==0){
                strongestAnimal=animal;
                candidates.add(animal);
            }else if(strongestAnimal.getEnergy()<animal.getEnergy()){
                candidates.clear();
                strongestAnimal=animal;
                animals.add(animal);
            } else if (strongestAnimal.getEnergy()==animal.getEnergy()) {
                candidates.add(animal);
            }
        }
        if (candidates.size()==1){
            return candidates.get(0);
        }
        
    }
    public void feedAnimals(){
       for (Vector2d key: plants.keySet()) {
           if (isanimal(key)){
               List<Animal> animals= new ArrayList<>(animalsAt(key));
               if(animals.size()==1){
                   animals.get(0).eat(conf.plantsEnergyValue());
                   }
               }else {
                    Animal animal = findStrongest(animals);
           }
       }
    }
}
