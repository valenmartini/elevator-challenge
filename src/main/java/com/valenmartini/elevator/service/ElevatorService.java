package com.valenmartini.elevator.service;

import com.valenmartini.elevator.exception.ElevatorException;
import com.valenmartini.elevator.exception.OverweightException;
import com.valenmartini.elevator.exception.SecurityException;
import com.valenmartini.elevator.model.Elevator;
import com.valenmartini.elevator.model.FreightElevator;
import com.valenmartini.elevator.model.KeyCard;
import com.valenmartini.elevator.model.PublicElevator;

import java.util.HashMap;
import java.util.Map;

public class ElevatorService {
    
    private final Map<Integer, Elevator> elevators = new HashMap<>();
    private final KeyCardService keyCardService;
    
    public ElevatorService(KeyCardService keyCardService) {
        this.keyCardService = keyCardService;
        initializeElevators();
    }
    
    private void initializeElevators() {
        PublicElevator publicElevator = new PublicElevator(1, keyCardService);
        elevators.put(publicElevator.getId(), publicElevator);
        
        FreightElevator freightElevator = new FreightElevator(2);
        elevators.put(freightElevator.getId(), freightElevator);
        
        System.out.println(String.format("Elevator system initialized with %s elevators", elevators.size()));
    }
    
    public boolean requestPublicElevator(int floor) throws ElevatorException {
        PublicElevator publicElevator = getPublicElevator();
        
        if (floor == 50 || floor == -1) {
            throw new SecurityException("Keycard required for floor " + floor);
        }
        
        boolean success = publicElevator.moveToFloor(floor);
        if (!success) {
            throw new ElevatorException("Failed to move public elevator to floor " + floor);
        }
        
        return true;
    }
    
    public boolean requestPublicElevatorWithKeyCard(int floor, KeyCard keyCard) 
            throws SecurityException, ElevatorException {
        PublicElevator publicElevator = getPublicElevator();

        boolean success = publicElevator.moveToRestrictedFloor(floor, keyCard);
        if (!success) {
            throw new ElevatorException("Failed to move public elevator to floor " + floor);
        }
        
        return true;
    }

    public boolean requestFreightElevator(int floor) throws ElevatorException {
        FreightElevator freightElevator = getFreightElevator();
        
        boolean success = freightElevator.moveToFloor(floor);
        if (!success) {
            throw new ElevatorException("Failed to move freight elevator to floor " + floor);
        }
        
        return true;
    }
    
    public void loadElevator(int elevatorId, double weightInKg) 
            throws ElevatorException, OverweightException {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.loadWeight(weightInKg);
    }

    public void unloadElevator(int elevatorId, double weightInKg) throws ElevatorException {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.unloadWeight(weightInKg);
    }
    
    public int getElevatorPosition(int elevatorId) throws ElevatorException {
        Elevator elevator = getElevatorById(elevatorId);
        return elevator.getCurrentFloor();
    }
    
    public double getElevatorCurrentWeight(int elevatorId) throws ElevatorException {
        Elevator elevator = getElevatorById(elevatorId);
        return elevator.getCurrentWeight();
    }
    
    public boolean isAlarmActive(int elevatorId) throws ElevatorException {
        Elevator elevator = getElevatorById(elevatorId);
        return elevator.isAlarmActive();
    }
    
    public boolean isShutdownActive(int elevatorId) throws ElevatorException {
        Elevator elevator = getElevatorById(elevatorId);
        return elevator.isShutdownActive();
    }

    private Elevator getElevatorById(int elevatorId) throws ElevatorException {
        Elevator elevator = elevators.get(elevatorId);
        if (elevator == null) {
            System.out.println(String.format("Elevator not found: %s", elevatorId));
            throw new ElevatorException("Elevator not found: " + elevatorId);
        }
        return elevator;
    }

    private PublicElevator getPublicElevator() throws ElevatorException {
        Elevator elevator = elevators.get(1);
        if (!(elevator instanceof PublicElevator)) {
            System.out.println(String.format("Public elevator not found or invalid type"));
            throw new ElevatorException("Public elevator not found or invalid type");
        }
        return (PublicElevator) elevator;
    }
    
    private FreightElevator getFreightElevator() throws ElevatorException {
        Elevator elevator = elevators.get(2);
        if (!(elevator instanceof FreightElevator)) {
            System.out.println(String.format("Freight elevator not found or invalid type"));
            throw new ElevatorException("Freight elevator not found or invalid type");
        }
        return (FreightElevator) elevator;
    }
}
