package agh.ics.oop.model;

import java.util.List;

public record ListQuantity(List<Integer> list,Integer quantity) {
    @Override
    public String toString() {
        return "Genome: " + list +
                ", quantity=" + quantity +
                '\n';
    }
}
