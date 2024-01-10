package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;

public class Genome {
    private static final int MIN_GENE = 0;
    private static final int MAX_GENE = 7;
    private final List<Integer> genes;
    private final boolean isBackAndForth;
    private final int minMutations;
    private final int maxMutations;
    private int currentGeneIndex = 0;
    private boolean sequencingDirection;

    public Genome(boolean isBackAndForth, int minMutations, int maxMutations, int size) {
        this.isBackAndForth = isBackAndForth;
        this.sequencingDirection = isBackAndForth;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genes = RandomNumGenerator.generateRandomIntList(MIN_GENE, MAX_GENE, size);
        currentGeneIndex = RandomNumGenerator.generateRandomInt(0, size-1);
    }

    public Genome(boolean isBackAndForth, int minMutations, int maxMutations, List<Integer> genes) {
        this.isBackAndForth = isBackAndForth;
        this.sequencingDirection = isBackAndForth;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genes = genes;
        currentGeneIndex = RandomNumGenerator.generateRandomInt(0, genes.size()-1);
        mutate();
    }

    private void mutate() {
        if (minMutations >= 0 && maxMutations >= minMutations) {
            int k = RandomNumGenerator.generateRandomInt(minMutations, maxMutations);
            List<Integer> genesToMutate = RandomNumGenerator.generateRandomUniqueIndexes(k, genes.size());

            genesToMutate.forEach((gene) -> {
                genes.set(gene, RandomNumGenerator.generateRandomIntWithoutK(MIN_GENE, MAX_GENE, genes.get(gene)));
            });
        } else {
            throw new IllegalArgumentException();
        }
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
        if (sequencingDirection) {
            currentGeneIndex += 1;
            if (currentGeneIndex == genes.size() - 1) {
                sequencingDirection = false;
            }
        } else {
            currentGeneIndex -= 1;
            if (currentGeneIndex == 0) {
                sequencingDirection = true;
            }
        }
    }

    private void nextGene() {
        currentGeneIndex = (currentGeneIndex + 1) % genes.size();
    }

    public boolean isBackAndForth() {
        return isBackAndForth;
    }

    public int size() {
        return genes.size();
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}
