package com.valenmartini.elevator.model;

import com.valenmartini.elevator.exception.OverweightException;

public abstract class Elevator {
    
    private final int id;
    private final double weightLimit;
    private int currentFloor;
    private double currentWeight;
    private boolean alarmActive;
    private boolean shutdownActive;
    
    protected Elevator(int id, double weightLimit) {
        this.id = id;
        this.weightLimit = weightLimit;
        this.currentFloor = 0;
        this.currentWeight = 0.0;
        this.alarmActive = false;
        this.shutdownActive = false;
    }
    
    public Boolean moveToFloor(int requestedFloor) {
        if (shutdownActive) {
            System.out.println(String.format("Elevator %s is shut down. Remove weight.", id));
            return false;
        }
        
        if (requestedFloor < -1 || requestedFloor > 50) {
            System.out.println(String.format("Invalid floor: %2", requestedFloor));
            return false;
        }
        
        System.out.println(String.format("Elevator %s moving from floor %s to floor %s", 
                id, currentFloor, requestedFloor));
        
        this.currentFloor = requestedFloor;
        System.out.println(String.format("Elevator %s arrived at floor %s", id, currentFloor));
        return true;
    }
        
    public void loadWeight(double weightInKg) throws OverweightException {
        double currentWeight = getCurrentWeight();
        double newTotalWeight = currentWeight + weightInKg;
        
        System.out.println(String.format("Loading %skg into elevator %s. Current weight: %skg", 
                weightInKg, id, currentWeight));

        this.currentWeight = newTotalWeight;
        if (newTotalWeight > weightLimit) {
            activateAlarm();
            activateShutdown();
            System.out.println(String.format("Weight limit exceeded in elevator %s. Limit: %skg, Loaded: %skg", 
                    id, weightLimit, newTotalWeight));
            throw new OverweightException("Weight limit exceeded. Maximum: " + 
                    weightLimit + "kg, Loaded: " + newTotalWeight + "kg");
        }
        
        System.out.println(String.format("New weight in elevator %s: %skg", id, newTotalWeight));
    }

    public void unloadWeight(double weightInKg) {
        double currentWeight = getCurrentWeight();
        double newWeight = Math.max(0, currentWeight - weightInKg);
        
        this.currentWeight = newWeight;
        System.out.println(String.format("Unloaded %skg from elevator %s. New weight: %skg", 
                weightInKg, id, newWeight));
        
        if (newWeight <= weightLimit && (alarmActive || shutdownActive)) {
            deactivateAlarm();
            deactivateShutdown();
            System.out.println(String.format("Weight back under limit for elevator %s. Deactivating safety systems.", id));
        }
    }
    
    public void activateAlarm() {
        this.alarmActive = true;
        System.out.println(String.format("ALARM ACTIVATED for elevator %s", id));
    }
    
    public void deactivateAlarm() {
        this.alarmActive = false;
        System.out.println(String.format("Alarm deactivated for elevator %s", id));
    }
    
    public void activateShutdown() {
        this.shutdownActive = true;
        System.out.println(String.format("SHUTDOWN ACTIVATED for elevator %s", id));
    }
    
    public void deactivateShutdown() {
        this.shutdownActive = false;
        System.out.println(String.format("Shutdown deactivated for elevator %s", id));
    }
    
    public int getId() {
        return id;
    }
    
    public double getWeightLimit() {
        return weightLimit;
    }
    
    public double getCurrentWeight() {
        return currentWeight;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public boolean isAlarmActive() {
        return alarmActive;
    }
    
    public boolean isShutdownActive() {
        return shutdownActive;
    }
}
