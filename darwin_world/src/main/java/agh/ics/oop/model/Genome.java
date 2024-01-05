package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genome {
    private static final int minGene = 0;
    private static final int maxGene = 7;
    private final List<Integer> genes;
    private int currentGeneIndex=0;
    private final boolean isalternative;
    private boolean flag;



    public Genome(int n,boolean isalternative){
        this.genes= RandomNumGenerator.generateRandomIntList(minGene, maxGene, n);
        this.isalternative=isalternative;
        this.flag=isalternative;
    }

    public Genome(List<Integer> genes,int min,int max,boolean isalternative) {
        this.isalternative=isalternative;
        this.genes=mutate(genes,min,max);
        this.flag=isalternative;
    }
    public List<Integer> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    public boolean isIsalternative() {
        return isalternative;
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }
    public int getCurentGene(){
        int ind=this.currentGeneIndex;
        if(isalternative){
            nextGeneBackAndForth();
        }else{
            nextGene();
        }
        return genes.get(ind);

    }
    public List<Integer> getRightGenesSlice(int n){
        return new ArrayList<>(genes.subList( genes.size()-n,genes.size()));
    }
    public List<Integer> getLeftGenesSlice(int n){
        return new ArrayList<>(genes.subList(0,n));
    }

    private List<Integer> mutate(List<Integer> genes, int min, int max){
        if (min == 0 && max == 0) {
            return genes;
        }
        List<Integer> newGenes = new ArrayList<>(genes);

        int k = RandomNumGenerator.generateRandomInt(min, max);
        List<Integer> genesToMutate = RandomNumGenerator.generateRandomUniqueIndexes(k, genes.size());

        genesToMutate.forEach((gene) -> {
            int mutatedGene = RandomNumGenerator.generateRandomIntWithoutK(0, 7, newGenes.get(gene));
            newGenes.set(gene, mutatedGene);
        });

        return newGenes;
    }
    private void nextGene(){
        currentGeneIndex=(currentGeneIndex+1)%genes.size();
    }
    private void nextGeneBackAndForth(){
        if(flag){
            currentGeneIndex+=1;
            if(currentGeneIndex==genes.size()-1){
                flag=false;
            }
        }else{
            currentGeneIndex-=1;
            if(currentGeneIndex==0){
                flag=true;
            }
        }
    }


    @Override
    public String toString() {
        return genes.toString();
    }
}
