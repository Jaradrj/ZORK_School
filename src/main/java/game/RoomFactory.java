package game;

import rooms.*;

public class RoomFactory {
    public static Room createRoom(String name) {
        return switch (name) {
            case "main entrance hall" -> new MainEntranceRoom();
            case "music room" -> new MusicRoom();
            case "printer room" -> new PrinterRoom();
            case "secretary" -> new Secretary();
            case "cafeteria" -> new Cafeteria();
            case "garage" -> new Garage();
            case "it room" -> new ITRoom();
            case "sportshall" -> new Sportshall();
            case "chemistry room" -> new ChemistryRoom();
            case "electricity room" -> new ElectricityRoom();
            case "teacher room" -> new TeacherRoom();
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
