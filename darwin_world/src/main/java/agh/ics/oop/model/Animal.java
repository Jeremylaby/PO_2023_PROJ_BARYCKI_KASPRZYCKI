package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.*;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int kidsNumber = 0;
    private int descendantsNumber = 0;
    private final Genome genome;
    private int energy;
    private final Animal father;
    private final Animal mother;
    private int age = 0;
    private int dayOfDeath = 0;
    private int plantsEaten = 0;

    public Animal(Vector2d position, Genome genome, int energy, Animal father, Animal mother) {
        this.position = position;
        this.genome = genome;
        this.energy = energy;
        this.father = father;
        this.mother = mother;
        this.orientation = MapDirection.NORTH.rotate(RandomNumGenerator.generateRandomInt(0, 7));
    }

    public Animal(Vector2d position, Genome genome, int energy) {
        this(position, genome, energy, null, null);
    }

    private void dfs(Animal animal, Set<Animal> visited) {
        if (animal == null || visited.contains(animal)) {
            return;
        }
        animal.descendantsNumber += 1;
        visited.add(animal);
        dfs(animal.mother, visited);
        dfs(animal.father, visited);
    }

    private void updateFamilyTree() {//todo
        Set<Animal> visited = new HashSet<>();
        dfs(this, visited);
        descendantsNumber = 0;
        mother.kidsNumber += 1;
        father.kidsNumber += 1;//potomkowie to dzieci+ inni potomkowie więc musze dodać jeszcz dzieci
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

    public void move(int width, int height) {
        age += 1;
        updateEnergy(-1);
        rotate(genome.getCurrentGene());
        updatePosition(width, height, position.add(orientation.toUnitVector()));
    }
    public void dodge(int width,int height,int n){
        rotate(n);
        updatePosition(width, height, position.add(orientation.toUnitVector()));
    }

    private void updatePosition(int width, int height, Vector2d newPosition) {
        if (newPosition.getY() >= 0 && newPosition.getY() <= height - 1) {
            if (newPosition.getX() < 0) {
                position = new Vector2d(width - 1, newPosition.getY());
            } else if (newPosition.getX() > width - 1) {
                position = new Vector2d(0, newPosition.getY());

            } else {
                position = newPosition;
            }
        } else {
            orientation = orientation.opposite();
        }
    }


    public Animal makeChild(Animal animal, int reproduceCost) {//w założeniu wywyołujemy tą metodę jeśli wiemy że this jest silniejszy
        Animal father = this;
        Animal mother = animal;

        father.updateEnergy(-reproduceCost);
        mother.updateEnergy(-reproduceCost);

        int k = Math.round(genome.size() * ((float) father.getEnergy() / (father.getEnergy() + mother.getEnergy())));
        int l = genome.size() - k;
        List<Integer> childGenes;

        if (RandomNumGenerator.generateRandomInt(0, 1) == 0) {
            childGenes = new ArrayList<>(father.genome.getLeftGenesSlice(k));
            childGenes.addAll(mother.genome.getRightGenesSlice(l));
        } else {
            childGenes = new ArrayList<>(mother.genome.getLeftGenesSlice(l));
            childGenes.addAll(father.genome.getRightGenesSlice(k));
        }

        Genome childGenome = new Genome(genome.isBackAndForth(), genome.getMinMutations(), genome.getMaxMutations(), childGenes);
        Animal child = new Animal(position, childGenome, 2 * reproduceCost, father, mother);//todo
        child.updateFamilyTree();
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

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

}
