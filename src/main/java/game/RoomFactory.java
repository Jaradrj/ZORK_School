package game;

import rooms.*;

public class RoomFactory {
    public static Room createRoom(String name) {
        return switch (name) {
            case "Main Entrance" -> new MainEntranceRoom();
            case "Music Room" -> new MusicRoom();
            case "Printer Room" -> new PrinterRoom();
            case "Secretary" -> new Secretary();
            case "Cafeteria" -> new Cafeteria();
            case "Garage" -> new Garage();
            case "ITRoom" -> new ITRoom();
            case "Sportshall" -> new Sportshall();
            case "Chemistry Room" -> new ChemistryRoom();
            case "Electricity Room" -> new ElectricityRoom();
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
