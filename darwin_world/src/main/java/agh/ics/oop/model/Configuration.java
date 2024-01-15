package agh.ics.oop.model;

public record Configuration(int mapWidth,
                            int mapHeight,
                            int plantsStartNum,
                            int plantsEnergyValue,
                            int plantsNumPerDay,
                            boolean plantsGrowthVariantPoison,
                            int animalsStartNum,
                            int animalsStartEnergy,
                            int animalsEnergyToReproduce,
                            int animalsEnergyReproduceCost,
                            int mutationsMinNum,
                            int mutationsMaxNum,
                            int genomeSize,
                            boolean genomeSequenceVariantBackAndForth) {
}
