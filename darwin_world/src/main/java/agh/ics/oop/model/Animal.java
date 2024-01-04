package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomNumGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animal implements WorldElement{
    private MapDirection orientation;
    private Vector2d position;
    private int kidsnumber=0;
    private final Genome genome;
    private final int genomeMin;
    private final  int genomeMax;
    private int energy;
    private List<Animal> parents=new ArrayList<>();
    private int age=0;
    private int dayOfDeath=0;
    private int plantsEaten=0;
    private final int genomeLength;
    private final boolean customNextGene;//wydaje mi się że tych 2 rzeczy nie musimy przechowywać
    private final int startingEnergy;

    public Animal(Vector2d position, int genomeMin, int genomeMax, int genomeLength, boolean customNextGene, int energy) {
        this.startingEnergy = energy;
        this.genome=new Genome(genomeLength,customNextGene);
        this.genomeLength=genomeLength;
        this.customNextGene=customNextGene;
        this.genomeMin=genomeMin;
        this.genomeMax=genomeMax;
        this.orientation = MapDirection.NORTH;
        this.position=position;
        this.energy=energy;
        this.parents=List.of(null,null);
    }

    public Animal(Vector2d position, Genome genome, int genomeMin, int genomeMax, int energy,Animal father,Animal mother) {
        this.startingEnergy = energy;
        this.orientation = MapDirection.NORTH;
        this.position = position;
        this.genome = genome;
        this.genomeMin = genomeMin;
        this.genomeMax = genomeMax;
        this.genomeLength = genome.getGenes().size();
        this.customNextGene = genome.isIsalternative();
        this.energy=energy;
        this.parents=List.of(father,mother);
    }

    public MapDirection getOrientation() {
        return orientation;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getKidsnumber() {
        return kidsnumber;
    }

    public Genome getGenome() {
        return genome;
    }

    public int getGenomeMin() {
        return genomeMin;
    }

    public int getGenomeMax() {
        return genomeMax;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return orientation.toString();
    }
    public boolean isAt(Vector2d position){
        return Objects.equals(this.position,position);
    }
    public void rotate(){
        orientation=(orientation.rotate(genome.getCurentGene()));
    }
    public void eat(int n){
        plantsEaten+=1;
        updateEnergy(n);
    }

    private void updateEnergy(int n) {
        this.energy+= n;
    }

    public void move(int width,int height){
        this.age+=1;
        this.rotate();
        Vector2d newposition=position.add(orientation.toUnitVector());
        if (newposition.getY()>=0&&newposition.getY()<=height-1){
            if(newposition.getX()<0){
                position=new Vector2d(width-1, newposition.getY());
            } else if (newposition.getX()>width-1) {
                position=new Vector2d(0, newposition.getY());

            }else {
                position = newposition;
            }
        }else {
            orientation = orientation.opposite();
        }
    }
    public Animal makeChild(Animal animal){//w założeniu wywyołujemy tą metodę jeśli wiemy że this jest silniejszy
        updateFamilyTree(this,animal);
        Animal father =this;
        Animal mother=animal;

        int k=Math.round((float) (genomeLength * father.getEnergy()) /mother.getEnergy());
        int l=genomeLength-k;
        List<Integer> fatherGenes=new ArrayList<>();
        List<Integer> motherGenes=new ArrayList<>();
        if(RandomNumGenerator.generateRandomInt(0,1)==0){
            fatherGenes=father.genome.getLeftGenesSlice(k);
            motherGenes=mother.genome.getRightGenesSlice(l);
        }else {
            fatherGenes = father.genome.getRightGenesSlice(k);
            motherGenes = mother.genome.getLeftGenesSlice(l);
        }
        List<Integer> childGenes=new ArrayList<>(fatherGenes);
        childGenes.addAll(motherGenes);
        Genome childGenome=new Genome(childGenes,genomeMin,genomeMax,customNextGene);
        return new Animal(position,childGenome,genomeMin,genomeMax,startingEnergy,father,mother);//todo

    }

    private void updateFamilyTree(Animal mother, Animal father) {//todo
        mother.kidsnumber=+1;
        father.kidsnumber=+1;

    }
    public void die(int n){
        this.dayOfDeath=n;
    }
}
