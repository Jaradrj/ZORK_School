package ui.game;

import console.game.Commands;
import console.rooms.*;
import console.game.Room;
import ui.controller.UIGameController;
import ui.rooms.UIElectricityRoom;
import ui.rooms.UITeacherRoom;

public class UIRoomFactory {

    private static UIGameController controller;
    private static Commands commands;

    public static void setController(UIGameController controller) {
        UIRoomFactory.controller = controller;
    }
    public static void setCommands(Commands commands) {
        UIRoomFactory.commands = commands;
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
            case "electricity room" -> new UIElectricityRoom(controller,commands);
            case "teacher room" -> new UITeacherRoom(controller, commands);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
