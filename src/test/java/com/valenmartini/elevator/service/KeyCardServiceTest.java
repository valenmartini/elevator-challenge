package com.valenmartini.elevator.service;

import com.valenmartini.elevator.exception.SecurityException;
import com.valenmartini.elevator.model.KeyCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyCardServiceTest {

    private KeyCardService keyCardService;
    private KeyCard keyCard;

    @BeforeEach
    public void setUp() {
        keyCardService = new KeyCardService();
        keyCard = keyCardService.issueKeyCard();
    }

    @Test
    public void testIssueKeyCard() {
        assertNotNull(keyCard);
        assertNotNull(keyCard.getId());
    }

    @Test
    public void testValidateAccessWithValidKeyCard() throws SecurityException {
        keyCardService.authorizeForFloor(keyCard.getId(), 10);
        assertTrue(keyCardService.validateAccess(keyCard, 10));
    }

    @Test
    public void testValidateAccessWithInvalidKeyCard() {
        KeyCard invalidKeyCard = new KeyCard();
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            keyCardService.validateAccess(invalidKeyCard, 10);
        });
        assertEquals("Unregistered keycard", exception.getMessage());
    }

    @Test
    public void testAuthorizeForFloor() throws SecurityException {
        keyCardService.authorizeForFloor(keyCard.getId(), 20);
        assertTrue(keyCard.canAccessFloor(20));
    }

    @Test
    public void testRevokeFloorAccess() throws SecurityException {
        keyCardService.authorizeForFloor(keyCard.getId(), 30);
        assertTrue(keyCard.canAccessFloor(30));
        keyCardService.revokeFloorAccess(keyCard.getId(), 30);
        assertFalse(keyCard.canAccessFloor(30));
    }
}
