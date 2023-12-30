package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;

public class Genome {
    private final List<Integer> genes;
    private int currentGeneIndex=0;
    private final boolean isalternative;
    private boolean flag;



    public Genome(int n,boolean isalternative){
        RandomNumGenerator randomNumGenerator=new RandomNumGenerator(0,7);
        List<Integer> list = randomNumGenerator.generateRandomIntList(n);
        this.genes=list;
        this.isalternative=isalternative;
        this.flag=isalternative;
    }
    public Genome(List<Integer> genes,int min,int max,boolean isalternative) {
        this.isalternative=isalternative;
        this.genes=mutate(genes,min,max);
        this.flag=isalternative;
    }
    public List<Integer> getGenes() {
        return List.copyOf(genes);
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

    private List<Integer> mutate(List<Integer> genes, int min,int max){
        if(min==0 && max==0){
            return genes;
        }
        List<Integer> newGenes=new ArrayList<>(genes);
        RandomNumGenerator randomNumGenerator=new RandomNumGenerator(min,max);
        int n = genes.size();
        int k = randomNumGenerator.generateRandomInt();
        RandomNumGenerator randomNumGenerator1=new RandomNumGenerator(0,n-1);
        RandomNumGenerator randomNumGenerator2=new RandomNumGenerator(0,7);
        List<Integer> genesToMutate=randomNumGenerator1.generateRandomIntListK(k,n);
        genesToMutate.forEach((gene)-> {
            int mutatedGene = randomNumGenerator2.generateRandomIntWithoutK(newGenes.get(gene));
            newGenes.set(gene,mutatedGene);
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
