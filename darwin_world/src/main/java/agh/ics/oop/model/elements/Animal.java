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
    private final AnimalColor color;
    private int kidsNumber = 0;
    private int descendantsNumber = 0;
    private int age = 0;
    private int dayOfDeath = 0;
    private int plantsEaten = 0;

    public Animal(Vector2d position, Genome genome, int energy, Animal father, Animal mother, int energyToReproduce) {
        this.position = position;
        this.genome = genome;
        this.energy = energy;
        this.father = father;
        this.mother = mother;
        this.color = new AnimalColor(this, energyToReproduce);
        this.orientation = MapDirection.NORTH.rotate(RandomNumGenerator.randomInt(0, 7));
    }

    public Animal(Vector2d position, Genome genome, int energy, int energyToReproduce) {
        this(position, genome, energy, null, null, energyToReproduce);
    }

    public void move(int width, int height) {
        if (energy <= 0) return;
        age += 1;
        updateEnergy(-1);
        rotate(genome.sequenceCurrentGene());
        updatePosition(width, height);
    }

    public void dodge(int width, int height, int n){
        rotate(n);
        updatePosition(width, height);
    }

    private void rotate(int n) {
        orientation = orientation.rotate(n);
    }

    public void eat(int foodEnergy) {
        plantsEaten += 1;
        updateEnergy(foodEnergy);
    }

    void updateEnergy(int value) {
        energy += value;
    }

    private void updatePosition(int width, int height) {
        Vector2d newPosition = position.add(orientation.toUnitVector());

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
            updatePosition(width, height);
        }
    }

    void kidsNumberIncrement() {
        kidsNumber++;
    }

    void descendantsNumberIncrement() {
        descendantsNumber++;
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

    Animal getFather() {
        return father;
    }

    Animal getMother() {
        return mother;
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

    public List<Integer> getGenes() {
        return genome.getGenes();
    }

    List<Integer> getLeftGenesSlice(int numOfGenes) {
        return genome.getLeftGenesSlice(numOfGenes);
    }

    List<Integer> getRightGenesSlice(int numOfGenes) {
        return genome.getRightGenesSlice(numOfGenes);
    }

    @Override
    public String toString() {
        return "Animal:\n" +
                "\tgenome: " + genome + "\n" +
                "\tactive part of genome: " + genome.getCurrentGeneIndex() + "\n" +
                "\tactive gene: " + genome.getCurrentGene() + "\n" +
                "\tenergy: " + energy + "\n" +
                "\tkids number: " + kidsNumber + "\n" +
                "\tdescendants number: " + descendantsNumber + "\n" +
                "\tage: " + age + "\n" +
                "\tday of death: " + dayOfDeath + "\n" +
                "\tplants eaten: " + plantsEaten + "\n";
    }

    @Override
    public Color getColor() {
        return color.getColor();
    }

    @Override
    public boolean isSelectable() {
        return true;
    }
    public boolean isdominating(List<Integer> list){
        return this.genome.getGenes().equals(list);
    }
}
