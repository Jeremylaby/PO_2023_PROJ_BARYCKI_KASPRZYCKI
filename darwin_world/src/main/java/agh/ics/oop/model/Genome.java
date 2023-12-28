package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;

public class Genome {
    private List<Integer> genes;
    private int currentGeneIndex=0;
    private boolean isalternative;
    private boolean flag;



    public Genome(int n,boolean isalternative){
        RandomNumGenerator randomNumGenerator=new RandomNumGenerator(0,7);
        List<Integer> list = randomNumGenerator.generateRandomIntList(n);
        this.genes=list;
        this.isalternative=isalternative;
    }
    public Genome(List<Integer> genes,int min,int max) {
        this.genes=genes;
        this.isalternative=isalternative;
        mutate(min,max);
    }
    public List<Integer> getGenes() {
        return List.copyOf(genes);
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }
    private void setGenes(List<Integer> genes) {
        this.genes = genes;
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

    private void mutate(int min,int max){
        List<Integer> newGenes=new ArrayList<>(this.getGenes());
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
        this.setGenes(newGenes);
    }
    private void nextGene(){
        currentGeneIndex+=1%genes.size();
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
