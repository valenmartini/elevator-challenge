package com.valenmartini.elevator;

import com.valenmartini.elevator.exception.ElevatorException;
import com.valenmartini.elevator.exception.OverweightException;
import com.valenmartini.elevator.model.KeyCard;
import com.valenmartini.elevator.service.ElevatorService;
import com.valenmartini.elevator.service.KeyCardService;

import java.util.Scanner;

public class ElevatorApplication {

    private final ElevatorService elevatorService;
    private final KeyCardService keyCardService;

    public ElevatorApplication() {
        this.keyCardService = new KeyCardService();
        this.elevatorService = new ElevatorService(keyCardService);

        System.out.println(String.format("Elevator app initialized "));
    }

    public ElevatorService getElevatorService() {
        return elevatorService;
    }

    public KeyCardService getKeyCardService() {
        return keyCardService;
    }

    public static void main(String[] args) {
        try {
            ElevatorApplication elevatorSystem = new ElevatorApplication();
            ElevatorService elevatorService = elevatorSystem.getElevatorService();
            KeyCardService keyCardService = elevatorSystem.getKeyCardService();

            KeyCard keyCard = keyCardService.issueKeyCard();
            keyCardService.authorizeForFloor(keyCard.getId(), 50);
            keyCardService.authorizeForFloor(keyCard.getId(), -1);

            try (Scanner scanner = new Scanner(System.in)) {
                boolean exit = false;

                while (!exit) {
                    System.out.println("Select an option:");
                    System.out.println("1. Move public elevator");
                    System.out.println("2. Move public elevator with keycard");
                    System.out.println("3. Move freight elevator");
                    System.out.println("4. Load elevator");
                    System.out.println("5. Unload elevator");
                    System.out.println("6. Check elevator positions");
                    System.out.println("7. Exit");

                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.println("Enter floor to move public elevator:");
                            int publicFloor = scanner.nextInt();
                            try {
                                elevatorService.requestPublicElevator(publicFloor);
                                System.out.println("Moved public elevator to floor " + publicFloor);
                            } catch (ElevatorException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 2:
                            System.out.println("Enter floor to move public elevator:");
                            int restrictedFloor = scanner.nextInt();
                            try {
                                elevatorService.requestPublicElevatorWithKeyCard(restrictedFloor, keyCard);
                                System.out
                                        .println("Moved public elevator to floor " + restrictedFloor + " with keycard");
                            } catch (ElevatorException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 3:
                            System.out.println("Enter floor to move freight elevator:");
                            int freightFloor = scanner.nextInt();
                            try {
                                elevatorService.requestFreightElevator(freightFloor);
                                System.out.println("Moved freight elevator to floor " + freightFloor);
                            } catch (ElevatorException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 4:
                            System.out.println("Enter elevator id (1 for public, 2 for freight):");
                            int elevatorId = scanner.nextInt();
                            System.out.println("Enter weight to load:");
                            int weight = scanner.nextInt();
                            try {
                                elevatorService.loadElevator(elevatorId, weight);
                                System.out.println("Loaded " + weight + "kg into " + elevatorId + " elevator");
                            } catch (OverweightException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 5:
                            System.out.println("Enter elevator id (1 for public, 2 for freight):");
                            int elevatorIdToUnload = scanner.nextInt();
                            System.out.println("Enter weight to unload:");
                            int weightToUnload = scanner.nextInt();
                            try {
                                elevatorService.unloadElevator(elevatorIdToUnload, weightToUnload);
                                System.out
                                        .println("Unloaded " + weightToUnload + "kg from " + elevatorIdToUnload
                                                + " elevator");
                            } catch (ElevatorException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        case 6:
                            System.out.println("Public elevator position: " + elevatorService.getElevatorPosition(1));
                            System.out.println("Freight elevator position: " + elevatorService.getElevatorPosition(2));
                            break;
                        case 7:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                }
            }

            System.out.println("Elevator system demonstration completed");
        } catch (Exception e) {
            System.out.println("Error during elevator system demonstration: " + e);
        }
    }
}
