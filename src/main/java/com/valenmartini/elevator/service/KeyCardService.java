package com.valenmartini.elevator.service;

import com.valenmartini.elevator.exception.SecurityException;
import com.valenmartini.elevator.model.KeyCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KeyCardService {
    
    private final Map<String, KeyCard> keyCards = new HashMap<>();
    
    public KeyCard issueKeyCard() {
        KeyCard keyCard = new KeyCard();
        keyCards.put(keyCard.getId(), keyCard);
        System.out.println(String.format("Issued new keycard %s", keyCard.getId()));
        return keyCard;
    }
    
    public Boolean validateAccess(KeyCard keyCard, int floor) throws SecurityException {
        KeyCard registeredCard = keyCards.get(keyCard.getId());
        if (registeredCard == null) {
            System.out.println(String.format("Unregistered keycard: %s", keyCard.getId()));
            throw new SecurityException("Unregistered keycard");
        }
        
        boolean hasAccess = registeredCard.canAccessFloor(floor);
        if (hasAccess) {
            System.out.println(String.format("Keycard %s granted access to floor %s", keyCard.getId(), floor));
        } else {
            System.out.println(String.format("Keycard %s denied access to floor %s", keyCard.getId(), floor));
        }
        
        return hasAccess;
    }
    
    public void authorizeForFloor(String keyCardId, int floor) throws SecurityException {
        KeyCard keyCard = getKeyCardById(keyCardId).orElseThrow(() -> 
                new SecurityException("Keycard not found: " + keyCardId));
        
        keyCard.authorizeForFloor(floor);
        System.out.println(String.format("Keycard %s authorized for floor %s", keyCardId, floor));
    }
    
    public void revokeFloorAccess(String keyCardId, int floor) throws SecurityException {
        KeyCard keyCard = getKeyCardById(keyCardId).orElseThrow(() -> 
                new SecurityException("Keycard not found: " + keyCardId));
        
        keyCard.revokeFloorAccess(floor);
        System.out.println(String.format("Keycard %s access revoked for floor %s", keyCardId, floor));
    }
    
    public Optional<KeyCard> getKeyCardById(String keyCardId) {
        return Optional.ofNullable(keyCards.get(keyCardId));
    }
}
