package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.RandomNumGenerator;
import javafx.scene.paint.Color;

import java.util.*;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private final Animal father;
    private final Animal mother;
    private int kidsNumber = 0;
    private int descendantsNumber = 0;
    private int age = 0;
    private int dayOfDeath = 0;
    private int plantsEaten = 0;

    public Animal(Vector2d position, Genome genome, int energy, Animal father, Animal mother) {
        this.position = position;
        this.genome = genome;
        this.energy = energy;
        this.father = father;
        this.mother = mother;
        this.orientation = MapDirection.NORTH.rotate(RandomNumGenerator.randomInt(0, 7));
    }

    public Animal(Vector2d position, Genome genome, int energy) {
        this(position, genome, energy, null, null);
    }

    private void familyTreeDFS(Animal animal, Set<Animal> visited) {
        if (animal == null || visited.contains(animal)) {
            return;
        }
        animal.descendantsNumber += 1;
        visited.add(animal);
        familyTreeDFS(animal.mother, visited);
        familyTreeDFS(animal.father, visited);
    }

    private void updateFamilyTree() {
        Set<Animal> visited = new HashSet<>();
        mother.kidsNumber += 1;
        father.kidsNumber += 1;
        familyTreeDFS(mother, visited);
        familyTreeDFS(father, visited);
    }

    public void move(int width, int height) {
        if (energy <= 0) return;
        age += 1;
        updateEnergy(-1);
        rotate(genome.sequenceCurrentGene());
        updatePosition(width, height, position.add(orientation.toUnitVector()));
    }

    public void dodge(int width, int height, int n){
        rotate(n);
        updatePosition(width, height, position.add(orientation.toUnitVector()));
    }

    private void rotate(int n) {
        orientation = orientation.rotate(n);
    }

    public void eat(int foodEnergy) {
        plantsEaten += 1;
        updateEnergy(foodEnergy);
    }

    private void updateEnergy(int value) {
        energy += value;
    }

    private void updatePosition(int width, int height, Vector2d newPosition) {
        if (newPosition.y() >= 0 && newPosition.y() <= height - 1) {
            if (newPosition.x() < 0) {
                position = new Vector2d(width - 1, newPosition.y());
            } else if (newPosition.x() > width - 1) {
                position = new Vector2d(0, newPosition.y());
            } else {
                position = newPosition;
            }
        } else {
            orientation = orientation.opposite();
            updatePosition(width, height, position.add(orientation.toUnitVector()));
        }
    }

    public Animal makeChild(Animal animal, int reproduceCost) {//w założeniu wywyołujemy tą metodę jeśli wiemy że this jest silniejszy
        Animal father = this;
        Animal mother = animal;

        if (mother.getEnergy() > father.getEnergy()) {
            mother = this;
            father = animal;
        }

        int k = Math.round(genome.size() * ((float) father.getEnergy() / (father.getEnergy() + mother.getEnergy())));
        int l = genome.size() - k;

        List<Integer> childGenes;

        if (RandomNumGenerator.randomInt(0, 1) == 0) {
            childGenes = new ArrayList<>(father.genome.getLeftGenesSlice(k));
            childGenes.addAll(mother.genome.getRightGenesSlice(l));
        } else {
            childGenes = new ArrayList<>(mother.genome.getLeftGenesSlice(l));
            childGenes.addAll(father.genome.getRightGenesSlice(k));
        }

        Genome childGenome = new Genome(genome.isBackAndForth(), genome.getMinMutations(), genome.getMaxMutations(), childGenes);
        childGenome.mutate();
        Animal child = new Animal(position, childGenome, 2 * reproduceCost, father, mother);//todo
        child.updateFamilyTree();

        father.updateEnergy(-reproduceCost);
        mother.updateEnergy(-reproduceCost);

        return child;
    }

    public void die(int day) {
        this.dayOfDeath = day;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getKidsNumber() {
        return kidsNumber;
    }

    public int getsDescendantNumber() {
        return descendantsNumber;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public List<Integer> getGenes() {
        return genome.getGenes();
    }

    @Override
    public String toString() {
        return "%d".formatted(getEnergy());
    }

    @Override
    public Color getColor() {
        return Color.hsb(30, Math.min(0.8, (double) energy / 100) + 0.2, 0.7);
    }

    @Override
    public boolean isSelectable() {
        return true;
    }
}
