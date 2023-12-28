package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;

public class Genome {
    private List<Integer> genes;
    private int currentGeneIndex=0;



    public Genome(int n){
        RandomNumGenerator randomNumGenerator=new RandomNumGenerator(0,7);
        List<Integer> list = randomNumGenerator.generateRandomIntList(n);
        this.genes=list;
    }
    public Genome(List<Integer> genes,int min,int max) {
        this.genes=genes;
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

    @Override
    public String toString() {
        return genes.toString();
    }
}
