package agh.ics.oop.model.elements;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimalsFactory {
    private final Configuration config;

    public AnimalsFactory(Configuration config) {
        this.config = config;
    }

    public Animal createInitialAnimal(Vector2d position) {
        Genome genome = new Genome(
                config.genomeSequenceVariantBackAndForth(),
                config.mutationsMinNum(),
                config.mutationsMaxNum(),
                config.genomeSize()
        );

        return new Animal(
                position,
                genome,
                config.animalsStartEnergy(),
                config.animalsEnergyToReproduce()
        );
    }

    public Animal makeChild(Animal father, Animal mother) {
        if (mother.getEnergy() > father.getEnergy()) {
            Animal temp = mother;
            mother = father;
            father = temp;
        }

        Animal child = createAnimal(father, mother, createChildGenes(father, mother));

        updateFamilyTree(child);

        father.updateEnergy(-config.animalsEnergyReproduceCost());
        mother.updateEnergy(-config.animalsEnergyReproduceCost());

        return child;
    }

    private static List<Integer> createChildGenes(Animal father, Animal mother) {
        float energyRatio = (float) father.getEnergy() / (father.getEnergy() + mother.getEnergy());

        int numOfFatherGenes = Math.round(father.getGenes().size() * energyRatio);
        int numOfMotherGenes = father.getGenes().size() - numOfFatherGenes;

        List<Integer> childGenes;

        if (RandomNumGenerator.randomInt(0, 1) == 0) {
            childGenes = new ArrayList<>(father.getLeftGenesSlice(numOfFatherGenes));
            childGenes.addAll(mother.getRightGenesSlice(numOfMotherGenes));
        } else {
            childGenes = new ArrayList<>(mother.getLeftGenesSlice(numOfMotherGenes));
            childGenes.addAll(father.getRightGenesSlice(numOfFatherGenes));
        }

        return childGenes;
    }

    private Animal createAnimal(Animal father, Animal mother, List<Integer> childGenes) {
        Genome genome = new Genome(
                config.genomeSequenceVariantBackAndForth(),
                config.mutationsMinNum(),
                config.mutationsMaxNum(),
                childGenes
        );
        genome.mutate();

        return new Animal(
                father.getPosition(),
                genome,
                2* config.animalsEnergyReproduceCost(),
                father,
                mother,
                config.animalsEnergyToReproduce()
        );
    }

    private void updateFamilyTree(Animal animal) {
        Set<Animal> visited = new HashSet<>();

        animal.getMother().kidsNumberIncrement();
        animal.getFather().kidsNumberIncrement();

        familyTreeDFS(animal.getMother(), visited);
        familyTreeDFS(animal.getFather(), visited);
    }

    private void familyTreeDFS(Animal animal, Set<Animal> visited) {
        if (animal == null || visited.contains(animal)) {
            return;
        }
        animal.descendantsNumberIncrement();
        visited.add(animal);

        familyTreeDFS(animal.getMother(), visited);
        familyTreeDFS(animal.getFather(), visited);
    }
}
