package agh.ics.oop.model.util;

import java.util.*;

public class RandomNumGenerator {
    private final int min;
    private final int max;

    public RandomNumGenerator(int min, int max) {
        if (max<min){
            throw new IllegalArgumentException("The (min) value have to be lower than the (max) value");
        }
        this.min = min;
        this.max = max;
    }

    public List<Integer> generateRandomIntList(int n){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<n;i++){
            list.add(generateRandomInt());
        }
        return list;
    }
    public List<Integer> generateRandomIntListK(int k,int n){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<n;i++){
            list.add(i);
        }
        Collections.shuffle(list);
        List<Integer> results=new ArrayList<>();
        for (int i=0;i<k;i++){
            results.add(list.get(i));
        }
        return results;
    }
    public int generateRandomInt(){
        Random random = new Random();
        return random.nextInt((max-min)+1)+min;
    }
    public int generateRandomIntWithoutK(int k){
        List<Integer> list=new ArrayList<>();
        for (int i=min;i<=max;i++){
            if(i!=k){
                list.add(i);
            }
        }
        Collections.shuffle(list);
        return list.get(0);
    }
}
