package com.valenmartini.elevator.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KeyCard {
    private final String id;
    private final Set<Integer> authorizedFloors;
    
    public KeyCard() {
        this.id = UUID.randomUUID().toString();
        this.authorizedFloors = new HashSet<>();
    }
    
    public void authorizeForFloor(int floor) {
        authorizedFloors.add(floor);
    }
    
    public void revokeFloorAccess(int floor) {
        authorizedFloors.remove(floor);
    }
    
    public boolean canAccessFloor(int floor) {
        return authorizedFloors.contains(floor);
    }
        
    public String getId() {
        return id;
    }
    
    public Set<Integer> getAuthorizedFloors() {
        return new HashSet<>(authorizedFloors); 
    }
}
