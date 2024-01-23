package agh.ics.oop.model.configuration;

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

    public static class Builder {
        private int mapWidth;
        private int mapHeight;
        private int plantsStartNum;
        private int plantsEnergyValue;
        private int plantsNumPerDay;
        private boolean plantsGrowthVariantPoison;
        private int animalsStartNum;
        private int animalsStartEnergy;
        private int animalsEnergyToReproduce = -1;
        private int animalsEnergyReproduceCost = -1;
        private int mutationsMinNum = -1;
        private int mutationsMaxNum = -1;
        private int genomeSize = -1;
        private boolean genomeSequenceVariantBackAndForth;

        public Builder() { }

        public Builder setMapWidth(int mapWidth) {
            this.mapWidth = mapWidth;
            return this;
        }

        public Builder setMapHeight(int mapHeight) {
            this.mapHeight = mapHeight;
            return this;
        }

        public Builder setPlantsStartNum(int plantsStartNum) {
            this.plantsStartNum = plantsStartNum;
            return this;
        }

        public Builder setPlantsEnergyValue(int plantsEnergyValue) {
            this.plantsEnergyValue = plantsEnergyValue;
            return this;
        }

        public Builder setPlantsNumPerDay(int plantsNumPerDay) {
            this.plantsNumPerDay = plantsNumPerDay;
            return this;
        }

        public Builder setPlantsGrowthVariantPoison(boolean plantsGrowthVariantPoison) {
            this.plantsGrowthVariantPoison = plantsGrowthVariantPoison;
            return this;
        }

        public Builder setAnimalsStartNum(int animalsStartNum) {
            this.animalsStartNum = animalsStartNum;
            return this;
        }

        public Builder setAnimalsStartEnergy(int animalsStartEnergy) {
            this.animalsStartEnergy = animalsStartEnergy;
            return this;
        }

        public Builder setAnimalsEnergyToReproduce(int animalsEnergyToReproduce) throws IllegalConfigurationValueException {
            if (animalsEnergyReproduceCost != -1 && animalsEnergyReproduceCost >= animalsEnergyToReproduce) {
                throw new IllegalConfigurationValueException(
                        animalsEnergyToReproduce,
                        "Energia zwierzęcia wymagana do reprodukcji",
                        "\"Energia zwierzęcia wymagana do reprodukcji\" powinna być większa niż \"Koszt reprodukcji zwierzęcie\""
                );
            }
            this.animalsEnergyToReproduce = animalsEnergyToReproduce;
            return this;
        }

        public Builder setAnimalsEnergyReproduceCost(int animalsEnergyReproduceCost) throws IllegalConfigurationValueException {
            if (animalsEnergyToReproduce != -1 && animalsEnergyReproduceCost >= animalsEnergyToReproduce) {
                throw new IllegalConfigurationValueException(
                        animalsEnergyReproduceCost,
                        "Koszt reprodukcji zwierzęcia",
                        "\"Koszt reprodukcji zwierzęcie\" powinien być mniejszy niż \"Energia zwierzęcia wymagana do reprodukcji\""
                );
            }
            this.animalsEnergyReproduceCost = animalsEnergyReproduceCost;
            return this;
        }

        public Builder setMutationsMinNum(int mutationsMinNum) throws IllegalConfigurationValueException {
            if (mutationsMaxNum != -1 && mutationsMinNum > mutationsMaxNum) {
                throw new IllegalConfigurationValueException(
                        mutationsMinNum,
                        "Minimalna liczba mutujących genów",
                        "\"Minimalna liczba mutujących genów\" powinna być mniejsza niż bądź równa \"Maksymalnej liczbie mutujących genów\""
                );
            }
            if (genomeSize != -1 && mutationsMinNum > genomeSize) {
                throw new IllegalConfigurationValueException(
                        mutationsMinNum,
                        "Minimalna liczba mutujących genów",
                        "\"Minimalna liczba mutujących genów\" powinna być mniejsza niż bądź równa \"Długości genomu zwierzęcia\""
                );
            }

            this.mutationsMinNum = mutationsMinNum;
            return this;
        }

        public Builder setMutationsMaxNum(int mutationsMaxNum) throws IllegalConfigurationValueException {
            if (mutationsMinNum != -1 && mutationsMinNum > mutationsMaxNum) {
                throw new IllegalConfigurationValueException(
                        mutationsMaxNum,
                        "Maksymalna liczba mutujących genów",
                        "\"Maksymalna liczba mutujących genów\" powinna być większe niż bądź równa \"Minimalnej liczbie mutujących genów\""
                );
            }
            this.mutationsMaxNum = mutationsMaxNum;
            return this;
        }

        public Builder setGenomeSize(int genomeSize) throws IllegalConfigurationValueException {
            if (mutationsMinNum != -1 && mutationsMinNum > genomeSize) {
                throw new IllegalConfigurationValueException(
                        mutationsMinNum,
                        "Minimalna liczba mutujących genów",
                        "\"Minimalna liczba mutujących genów\" powinna być mniejsza niż bądź równa \"Długości genomu zwierzęcia\""
                );
            }
            this.genomeSize = genomeSize;
            return this;
        }

        public Builder setGenomeSequenceVariantBackAndForth(boolean genomeSequenceVariantBackAndForth) {
            this.genomeSequenceVariantBackAndForth = genomeSequenceVariantBackAndForth;
            return this;
        }

        public Configuration build() {
            return new Configuration(
                    mapWidth,
                    mapHeight,
                    plantsStartNum,
                    plantsEnergyValue,
                    plantsNumPerDay,
                    plantsGrowthVariantPoison,
                    animalsStartNum,
                    animalsStartEnergy,
                    animalsEnergyToReproduce,
                    animalsEnergyReproduceCost,
                    mutationsMinNum,
                    mutationsMaxNum,
                    genomeSize,
                    genomeSequenceVariantBackAndForth
            );
        }
    }
}
