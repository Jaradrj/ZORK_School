package game;

import controller.GameController;
import rooms.*;

public class RoomFactory {

    private static GameController controller;

    public static void setController(GameController controller) {
        RoomFactory.controller = controller;
    }

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
            case "electricity room" -> new ElectricityRoom(controller);
            case "teacher room" -> new TeacherRoom(controller);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
