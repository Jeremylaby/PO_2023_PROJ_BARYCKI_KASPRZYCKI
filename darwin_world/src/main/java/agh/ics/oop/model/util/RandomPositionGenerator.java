package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth;
    private final int maxHeight;
    private final List<Vector2d> randompositions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int n) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        List<Vector2d> allposistions = new ArrayList<>();
        for(int i=0;i<=maxHeight;i++){
            for (int j=0;j<=maxWidth;j++){
                allposistions.add(new Vector2d(i,j));
            }
        }
        this.randompositions=randomPositions(n,allposistions);
    }
    private List<Vector2d> randomPositions(int k ,List<Vector2d> allpositions){
        int n=allpositions.size();
        List<Vector2d> randomised = new ArrayList<>();
        for(int i=0;i<k;i++){
            int l = (int) Math.floor(Math.random()*n);
            randomised.add(allpositions.get(l));
            allpositions.set(l,allpositions.get(n-1));
            n--;
        }
        return randomised;
    }

    @Override
    public Iterator<Vector2d> iterator() {

        return randompositions.iterator();
    }
}
