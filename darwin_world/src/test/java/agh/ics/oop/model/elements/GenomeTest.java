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
        var genes = List.of(1, 2, 3, 4, 5);
        var genome = new Genome(false, 0, 0, genes);
        int index = genome.getCurrentGeneIndex();

        int gene1 = genome.sequenceCurrentGene();
        int gene2 = genome.sequenceCurrentGene();

        assertEquals(genes.get(index), gene1);
        assertEquals(genes.get((index + 1) % genes.size()), gene2);
        assertEquals((index + 2) % genes.size(), genome.getCurrentGeneIndex());
    }

    @Test
    void sequenceCurrentGeneBackAndForth() throws NoSuchFieldException, IllegalAccessException {
        var genes = List.of(1, 2, 3, 4, 5);
        var genome = new Genome(true, 0, 0, genes);
        int index = 3;

        var geneIndexField = genome.getClass().getDeclaredField("currentGeneIndex");
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
        var genes = List.of(1, 2, 3, 4, 5, 6);
        var genome = new Genome(true, 0, 0, genes);

        var rightGenesSlice1 = genome.getRightGenesSlice(3);
        var rightGenesSlice2 = genome.getRightGenesSlice(4);

        assertEquals(List.of(4, 5, 6), rightGenesSlice1);
        assertEquals(List.of(3, 4, 5, 6), rightGenesSlice2);
    }

    @Test
    void getLeftGenesSlice() {
        var genes = List.of(1, 2, 3, 4, 5, 6);
        var genome = new Genome(true, 0, 0, genes);

        var leftGenesSlice1 = genome.getLeftGenesSlice(3);
        var leftGenesSlice2 = genome.getLeftGenesSlice(4);

        assertEquals(List.of(1, 2, 3), leftGenesSlice1);
        assertEquals(List.of(1, 2, 3, 4), leftGenesSlice2);
    }

    @Test
    void mutate() {
        var genes = List.of(1, 2, 3, 4, 5, 6);
        var genome = new Genome(true, 2, 4, new ArrayList<>(genes));

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