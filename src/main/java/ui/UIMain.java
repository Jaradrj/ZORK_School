package ui;

import console.game.RoomFactory;
import ui.controller.UIGameController;
import console.game.Commands;
import ui.game.UIRoomFactory;
import console.game.Room;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIMain {
    public static void main(String[] args) throws IOException {
        Map<String, Room> roomMap = new HashMap<>();
        Commands commands = new Commands(roomMap);
        UIRoomFactory.setCommands(commands);
        String[] roomNames = {
                "main entrance hall",
                "music room",
                "printer room",
                "secretary",
                "cafeteria",
                "garage",
                "it room",
                "sportshall",
                "chemistry room",
                "electricity room",
                "teacher room"
        };

        for (String name : roomNames) {
            roomMap.put(name, RoomFactory.createRoom(name));
        }

        UIGameController controller = new UIGameController(commands);
        UIRoomFactory.setController(controller);
        controller.run();
    }
}
