package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Genome {
    public static final int MIN_GENE = 0;
    public static final int MAX_GENE = 7;
    private final List<Integer> genes;
    private final boolean isBackAndForth;
    private final int minMutations;
    private final int maxMutations;
    private int currentGeneIndex;
    private boolean sequencingDirection;

    public Genome(boolean isBackAndForth, int minMutations, int maxMutations, List<Integer> genes) {
        this.isBackAndForth = isBackAndForth;
        this.sequencingDirection = isBackAndForth;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genes = genes;
        currentGeneIndex = RandomNumGenerator.randomInt(0, genes.size()-1);
    }

    public Genome(boolean isBackAndForth, int minMutations, int maxMutations, int size) {
        this(
                isBackAndForth,
                minMutations,
                maxMutations,
                RandomNumGenerator.randomIntList(MIN_GENE, MAX_GENE, size)
        );
    }

    public void mutate() {
        if (minMutations >= 0 && maxMutations >= minMutations) {
            int k = RandomNumGenerator.randomInt(minMutations, maxMutations);
            List<Integer> genesToMutate = RandomNumGenerator.randomUniqueIndexes(k, genes.size()-1);

            genesToMutate.forEach((gene) -> {
                genes.set(gene, RandomNumGenerator.randomIntWithoutK(MIN_GENE, MAX_GENE, genes.get(gene)));
            });
        } else {
            throw new IllegalArgumentException("minMutations must be less than maxMutations");
        }
    }

    public int sequenceCurrentGene() {
        int gene = genes.get(currentGeneIndex);
        sequenceGenes();
        return gene;
    }

    private void sequenceGenes() {
        if (isBackAndForth) {
            nextGeneBackAndForth();
        } else {
            nextGene();
        }
    }

    private void nextGeneBackAndForth() {
        if (sequencingDirection && currentGeneIndex == genes.size() - 1) {
            sequencingDirection = false;
        } else if (!sequencingDirection && currentGeneIndex == 0) {
            sequencingDirection = true;
        }
        currentGeneIndex += sequencingDirection ? 1 : -1;
    }

    private void nextGene() {
        currentGeneIndex = (currentGeneIndex + 1) % genes.size();
    }

    public List<Integer> getRightGenesSlice(int n) {
        return new ArrayList<>(genes.subList(genes.size() - n, genes.size()));
    }

    public List<Integer> getLeftGenesSlice(int n) {
        return new ArrayList<>(genes.subList(0, n));
    }

    public List<Integer> getGenes() {
        return Collections.unmodifiableList(genes);
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public int getCurrentGene() {
        return genes.get(currentGeneIndex);
    }

    @Override
    public String toString() {
        String exceededSize = genes.size() > 25 ? "..." : "";

        return genes.stream()
                .map(Object::toString)
                .collect(Collectors.joining("", "[", exceededSize + "]"));
    }
}
