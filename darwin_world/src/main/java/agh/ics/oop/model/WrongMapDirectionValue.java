package agh.ics.oop.model;

public class WrongMapDirectionValue extends Exception{
    WrongMapDirectionValue(int value){
        super("value "+value+ " is out of range");
    }
}
