package ui;

import console.game.Player;
import console.game.RoomFactory;
import ui.controller.UIGameController;
import console.game.Commands;
import ui.game.StartMenu;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIMain {

    public static void main(String[] args) {
        try {
            Player player = new Player();
            StartMenu startMenu = new StartMenu(player);
            startMenu.showStartMenu();

            Map<String, UIRoom> roomMap = new HashMap<>();
            String[] roomNames = {
                    "main entrance hall",
                    "music room",
                    "it room",
                    "cafeteria",
                    "chemistry room",
                    "printer room",
                    "sportshall",
                    "secretary",
                    "garage",
                    "teacher room"
            };

            for (String name : roomNames) {
                roomMap.put(name, UIRoomFactory.createRoom(name));
            }

            UICommands commands = new UICommands(roomMap);
            UIRoomFactory.setCommands(commands);

            UIGameController controller = new UIGameController(commands, player);
            UIRoomFactory.setController(controller);

            controller.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}