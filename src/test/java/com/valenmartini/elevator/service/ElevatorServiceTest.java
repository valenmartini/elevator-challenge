package com.valenmartini.elevator.service;

import com.valenmartini.elevator.exception.ElevatorException;
import com.valenmartini.elevator.exception.OverweightException;
import com.valenmartini.elevator.exception.SecurityException;
import com.valenmartini.elevator.model.KeyCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElevatorServiceTest {

    @Mock
    private KeyCardService keyCardService;

    private ElevatorService elevatorService;
    private KeyCard validKeyCard;
    private KeyCard invalidKeyCard;

    @BeforeEach
    public void setUp() {

        elevatorService = new ElevatorService(keyCardService);
        validKeyCard = new KeyCard();
        invalidKeyCard = new KeyCard();
    }

    @Test
    public void testRequestPublicElevatorToRegularFloor() throws ElevatorException {
        boolean result = elevatorService.requestPublicElevator(10);
        assertTrue(result);
        assertEquals(10, elevatorService.getElevatorPosition(1));
    }

    @Test
    public void testRequestPublicElevatorToRestrictedFloorWithoutKeycard() {
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            elevatorService.requestPublicElevator(50);
        });
        assertEquals("Keycard required for floor 50", exception.getMessage());
    }

    @Test
    public void testRequestPublicElevatorToRestrictedFloorWithValidKeycard()
            throws SecurityException, ElevatorException {
        try {
            when(keyCardService.validateAccess(eq(validKeyCard), eq(50))).thenReturn(true);
        } catch (SecurityException e) {
            fail("Exception during setup: " + e.getMessage());
        }
        boolean result = elevatorService.requestPublicElevatorWithKeyCard(50, validKeyCard);
        assertTrue(result);
        assertEquals(50, elevatorService.getElevatorPosition(1));
    }

    @Test
    public void testRequestPublicElevatorToRestrictedFloorWithInvalidKeycard() {
        try {
            when(keyCardService.validateAccess(eq(invalidKeyCard), any(Integer.class))).thenReturn(false);
        } catch (SecurityException e) {
            fail("Exception during setup: " + e.getMessage());
        }
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            elevatorService.requestPublicElevatorWithKeyCard(50, invalidKeyCard);
        });
        assertEquals("Keycard does not have access to floor 50", exception.getMessage());
    }

    @Test
    public void testRequestFreightElevator() throws ElevatorException {
        boolean result = elevatorService.requestFreightElevator(-1);
        assertTrue(result);
        assertEquals(-1, elevatorService.getElevatorPosition(2));
    }

    @Test
    public void testLoadPublicElevatorOverweight() throws ElevatorException {
        OverweightException exception = assertThrows(OverweightException.class, () -> {
            elevatorService.loadElevator(1, 1200);
        });
        assertTrue(exception.getMessage().contains("Weight limit exceeded"));
        assertTrue(elevatorService.isAlarmActive(1));
        assertTrue(elevatorService.isShutdownActive(1));
    }

    @Test
    public void testLoadFreightElevatorOverweight() throws ElevatorException {
        OverweightException exception = assertThrows(OverweightException.class, () -> {
            elevatorService.loadElevator(2, 3500); // Freight elevator limit is 3000kg
        });
        assertTrue(exception.getMessage().contains("Weight limit exceeded"));
        assertTrue(elevatorService.isAlarmActive(2));
        assertTrue(elevatorService.isShutdownActive(2));
    }

    @Test
    public void testLoadAndUnloadWeight() throws ElevatorException, OverweightException {
        elevatorService.loadElevator(1, 500);
        assertEquals(500, elevatorService.getElevatorCurrentWeight(1));

        elevatorService.unloadElevator(1, 300);
        assertEquals(200, elevatorService.getElevatorCurrentWeight(1));
    }

    @Test
    public void testOverloadAndUnloadElevator() throws ElevatorException {
        OverweightException exception = assertThrows(OverweightException.class, () -> {
            elevatorService.loadElevator(1, 1200);
        });
        assertTrue(exception.getMessage().contains("Weight limit exceeded"));
        assertTrue(elevatorService.isAlarmActive(1));
        assertTrue(elevatorService.isShutdownActive(1));

        elevatorService.unloadElevator(1, 300);
        assertEquals(900, elevatorService.getElevatorCurrentWeight(1));
        assertFalse(elevatorService.isAlarmActive(1));
        assertFalse(elevatorService.isShutdownActive(1));
    }

}
