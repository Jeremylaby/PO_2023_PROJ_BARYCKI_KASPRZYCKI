package agh.ics.oop.model;

import java.util.List;
import java.util.stream.Collectors;

public record ListQuantity(List<Integer> list,Integer quantity) {
    @Override
    public String toString() {
        return getListString() +
                ", quantity:" + quantity +
                '\n';
    }

    public String getListString() {
        return list.stream().map(Object::toString).collect(Collectors.joining("", "[", "]"));
    }
}
