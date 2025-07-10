package game;

import controller.GameController;
import rooms.*;

public class RoomFactory {

    private static GameController controller;
    private static Commands commands;

    public static void setController(GameController controller) {
        RoomFactory.controller = controller;
    }
    public static void setCommands(Commands commands) {
        RoomFactory.commands = commands;
    }

    public static Room createRoom(String name) {
        return switch (name) {
            case "main entrance hall" -> new MainEntranceRoom(commands);
            case "music room" -> new MusicRoom(commands);
            case "printer room" -> new PrinterRoom(commands);
            case "secretary" -> new Secretary(commands);
            case "cafeteria" -> new Cafeteria(commands);
            case "garage" -> new Garage();
            case "it room" -> new ITRoom(commands);
            case "sportshall" -> new Sportshall(commands);
            case "chemistry room" -> new ChemistryRoom(commands);
            case "electricity room" -> new ElectricityRoom(controller,commands);
            case "teacher room" -> new TeacherRoom(controller, commands);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
