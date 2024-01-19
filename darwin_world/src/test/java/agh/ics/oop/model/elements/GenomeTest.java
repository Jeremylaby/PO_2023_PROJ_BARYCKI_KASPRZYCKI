package agh.ics.oop.model.elements;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    void sequenceCurrentGene() {
        List<Integer> genes = List.of(1, 2, 3, 4, 5);
        Genome genome = new Genome(false, 0, 0, genes);
        int index = genome.getCurrentGeneIndex();

        int gene1 = genome.sequenceCurrentGene();
        int gene2 = genome.sequenceCurrentGene();

        assertEquals(genes.get(index), gene1);
        assertEquals(genes.get((index + 1) % genes.size()), gene2);
        assertEquals((index + 2) % genes.size(), genome.getCurrentGeneIndex());
    }

    @Test
    void sequenceCurrentGeneBackAndForth() throws NoSuchFieldException, IllegalAccessException {
        List<Integer> genes = List.of(1, 2, 3, 4, 5);
        Genome genome = new Genome(true, 0, 0, genes);
        int index = 3;

        Field geneIndexField = genome.getClass().getDeclaredField("currentGeneIndex");
        geneIndexField.setAccessible(true);
        geneIndexField.setInt(genome, index);

        int gene1 = genome.sequenceCurrentGene();
        int gene2 = genome.sequenceCurrentGene();
        int gene3 = genome.sequenceCurrentGene();

        assertEquals(4, gene1);
        assertEquals(5, gene2);
        assertEquals(4, gene3);
    }

    @Test
    void getRightGenesSlice() {
        List<Integer> genes = List.of(1, 2, 3, 4, 5, 6);
        Genome genome = new Genome(true, 0, 0, genes);

        List<Integer> rightGenesSlice1 = genome.getRightGenesSlice(3);
        List<Integer> rightGenesSlice2 = genome.getRightGenesSlice(4);

        assertEquals(List.of(4, 5, 6), rightGenesSlice1);
        assertEquals(List.of(3, 4, 5, 6), rightGenesSlice2);
    }

    @Test
    void getLeftGenesSlice() {
        List<Integer> genes = List.of(1, 2, 3, 4, 5, 6);
        Genome genome = new Genome(true, 0, 0, genes);

        List<Integer> leftGenesSlice1 = genome.getLeftGenesSlice(3);
        List<Integer> leftGenesSlice2 = genome.getLeftGenesSlice(4);

        assertEquals(List.of(1, 2, 3), leftGenesSlice1);
        assertEquals(List.of(1, 2, 3, 4), leftGenesSlice2);
    }

    @Test
    void mutate() {
        List<Integer> genes = List.of(1, 2, 3, 4, 5, 6);
        Genome genome = new Genome(true, 2, 4, new ArrayList<>(genes));

        genome.mutate();

        int commonCount = 0;
        for (int i = 0; i < genes.size(); i++) {
            if (Objects.equals(genes.get(i), genome.getGenes().get(i))) {
                commonCount++;
            }
        }

        assertTrue(commonCount >= 2);
        assertTrue(commonCount <= 4);
    }
}