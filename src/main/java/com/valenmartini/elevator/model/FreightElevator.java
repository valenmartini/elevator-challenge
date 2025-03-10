package com.valenmartini.elevator.model;

public class FreightElevator extends Elevator {
    
    private static final double WEIGHT_LIMIT_KG = 3000.0;
    
    public FreightElevator(int id) {
        super(id, WEIGHT_LIMIT_KG);
    }
}
