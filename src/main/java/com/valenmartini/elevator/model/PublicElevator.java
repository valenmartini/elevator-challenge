package com.valenmartini.elevator.model;

import com.valenmartini.elevator.exception.SecurityException;
import com.valenmartini.elevator.service.KeyCardService;

public class PublicElevator extends Elevator {
    
    private static final double WEIGHT_LIMIT_KG = 1000.0; 
    private final KeyCardService keyCardService;
    
    public PublicElevator(int id, KeyCardService keyCardService) {
        super(id, WEIGHT_LIMIT_KG);
        this.keyCardService = keyCardService;
    }
    
    public boolean moveToRestrictedFloor(int requestedFloor, KeyCard keyCard) throws SecurityException {
        if (requestedFloor != 50 && requestedFloor != -1) {
            return moveToFloor(requestedFloor);
        }
        
        if (keyCard == null) {
            System.out.println(String.format("Attempt to access floor %s without keycard", requestedFloor));
            throw new SecurityException("Keycard required for floor " + requestedFloor);
        }
        
        Boolean hasAccess = keyCardService.validateAccess(keyCard, requestedFloor);
        if (!hasAccess) {
            System.out.println(String.format("Keycard %s denied access to floor %s", keyCard.getId(), requestedFloor));
            throw new SecurityException("Keycard does not have access to floor " + requestedFloor);
        }
        
        System.out.println(String.format("Keycard %s granted access to floor %s", keyCard.getId(), requestedFloor));
        return super.moveToFloor(requestedFloor);
    }
}
