package agh.ics.oop.model.util;

import java.util.*;

public class RandomNumGenerator {

    public static List<Integer> generateRandomIntList(int min, int max, int n){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<n;i++){
            list.add(generateRandomInt(min,max));
        }
        return list;
    }
    public static List<Integer> generateRandomIntListK(int min, int max, int k, int n){
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
    public static int generateRandomInt(int min, int max){
        Random random = new Random();
        return random.nextInt((max-min)+1)+min;
    }
    public static int generateRandomIntWithoutK(int min, int max, int k){
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
