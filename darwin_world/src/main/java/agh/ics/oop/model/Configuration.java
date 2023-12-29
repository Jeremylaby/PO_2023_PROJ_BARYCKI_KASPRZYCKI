package agh.ics.oop.model;

public record Configuration(int mapWidth,
                            int mapHeight,
                            int plantsStartNum,
                            int plantsEnergyValue,
                            int plantsNumPerDay,
                            boolean customPlants,
                            int animalsStartNum,
                            int animalsStartEnergy,
                            int animalsEnergyToReproduce,
                            int animalsEnergyReproduceCost,
                            int genomeMin,
                            int genomeMax,
                            int genomeLength,
                            boolean customNextGene) {
}
