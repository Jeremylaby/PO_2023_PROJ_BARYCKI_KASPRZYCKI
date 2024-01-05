package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genome {
    private static final int MIN_GENE = 0;
    private static final int MAX_GENE = 7;
    private final List<Integer> genes;
    private int currentGeneIndex = 0;
    private final boolean isBackAndForth;
    private boolean flag;

    public Genome(int size, boolean isBackAndForth) {
        this.genes = RandomNumGenerator.generateRandomIntList(MIN_GENE, MAX_GENE, size);
        this.isBackAndForth = isBackAndForth;
        this.flag = isBackAndForth;
    }

    public Genome(List<Integer> genes, int minMutations, int maxMutations, boolean isBackAndForth) {
        this.genes = genes;
        this.isBackAndForth = isBackAndForth;
        this.flag = isBackAndForth;
        mutate(minMutations, maxMutations);
    }

    private void mutate(int min, int max) {
        if (min >= 0 && max >= min) {
            int k = RandomNumGenerator.generateRandomInt(min, max);
            List<Integer> genesToMutate = RandomNumGenerator.generateRandomUniqueIndexes(k, genes.size());

            genesToMutate.forEach((gene) -> {
                genes.set(gene, RandomNumGenerator.generateRandomIntWithoutK(MIN_GENE, MAX_GENE, genes.get(gene)));
            });
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<Integer> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    public int getCurrentGene() {
        int gene = genes.get(this.currentGeneIndex);
        sequenceGenes();
        return gene;
    }

    public List<Integer> getRightGenesSlice(int n) {
        return new ArrayList<>(genes.subList(genes.size() - n, genes.size()));
    }

    public List<Integer> getLeftGenesSlice(int n) {
        return new ArrayList<>(genes.subList(0, n));
    }

    private void sequenceGenes() {
        if (isBackAndForth) {
            nextGeneBackAndForth();
        } else {
            nextGene();
        }
    }

    private void nextGeneBackAndForth() {
        if (flag) {
            currentGeneIndex += 1;
            if (currentGeneIndex == genes.size() - 1) {
                flag = false;
            }
        } else {
            currentGeneIndex -= 1;
            if (currentGeneIndex == 0) {
                flag = true;
            }
        }
    }

    private void nextGene() {
        currentGeneIndex = (currentGeneIndex + 1) % genes.size();
    }

    public boolean isBackAndForth() {
        return isBackAndForth;
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}
