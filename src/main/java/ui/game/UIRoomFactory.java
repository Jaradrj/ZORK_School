package ui.game;

import console.game.Commands;
import console.rooms.*;
import console.game.Room;
import ui.controller.UIGameController;
import ui.rooms.*;

public class UIRoomFactory {

    private static UIGameController controller;
    private static UICommands commands;

    public static void setController(UIGameController controller) {
        UIRoomFactory.controller = controller;
    }
    public static void setCommands(UICommands commands) {
        UIRoomFactory.commands = commands;
    }

    public static UIRoom createRoom(String name) {
        return switch (name) {
            case "main entrance hall" -> new UIMainEntranceRoom(commands);
            case "music room" -> new UIMusicRoom(commands);
            case "it room" -> new UIITRoom(commands);
            case "cafeteria" -> new UICafeteria(commands);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
