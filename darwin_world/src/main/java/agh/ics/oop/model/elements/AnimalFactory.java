package agh.ics.oop.model.elements;

import agh.ics.oop.model.Configuration;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;

public class AnimalFactory {
    private final int reproduceCost;
    private final int energyToReproduce;
    private final boolean isGenomeSequenceBackAndForth;
    private final int minMutations;
    private final int maxMutations;

    public AnimalFactory(Configuration config) {
        this.reproduceCost = config.animalsEnergyReproduceCost();
        this.energyToReproduce = config.animalsEnergyToReproduce();
        this.isGenomeSequenceBackAndForth = config.genomeSequenceVariantBackAndForth();
        this.minMutations = config.mutationsMinNum();
        this.maxMutations = config.mapHeight();
    }

    public Animal makeChild(Animal father, Animal mother) {
        if (mother.getEnergy() > father.getEnergy()) {
            Animal temp = mother;
            mother = father;
            father = temp;
        }


        int k = Math.round(father.getGenes().size() * ((float) father.getEnergy() / (father.getEnergy() + mother.getEnergy())));
        int l = father.getGenes().size() - k;

        List<Integer> childGenes;

        if (RandomNumGenerator.randomInt(0, 1) == 0) {
            childGenes = new ArrayList<>(father.getLeftGenesSlice(k));
            childGenes.addAll(mother.getRightGenesSlice(l));
        } else {
            childGenes = new ArrayList<>(mother.getLeftGenesSlice(l));
            childGenes.addAll(father.getRightGenesSlice(k));
        }

        Genome childGenome = new Genome(isGenomeSequenceBackAndForth, minMutations, maxMutations, childGenes);
        childGenome.mutate();
        Animal child = new Animal(father.getPosition(), childGenome, 2 * reproduceCost, father, mother, energyToReproduce);

        child.updateFamilyTree();

        father.updateEnergy(-reproduceCost);
        mother.updateEnergy(-reproduceCost);

        return child;
    }
}
