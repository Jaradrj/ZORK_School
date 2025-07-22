package ui;

import console.game.Inventory;
import console.game.Player;
import console.game.RoomFactory;
import console.rooms.Sportshall;
import ui.components.Logos;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import console.game.Commands;
import ui.game.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIMain {

    public static int gameCount = 0;
    public static String playerName = "";

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {

        gameCount++;

        try {
            Player player = new Player();

            if(gameCount >= 2){
                player.setFlag("second_try");
            }

            StartMenu startMenu = new StartMenu(player);
            startMenu.showStartMenu();

            if(player.hasFlag("second_try")){
                player.setOldName(playerName);
            }

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
                    "teacher room",
                    "electricity room"
            };

            for (String name : roomNames) {
                roomMap.put(name, UIRoomFactory.createRoom(name));
            }

            TextPrinter printer = new TextPrinter();
            Logos logos = new Logos(printer);
            UIRoomFactory.setPrinter(printer);
            UIRoomFactory.setLogos(logos);

            UIGameController controller = new UIGameController(player);
            UIRoomFactory.setController(controller);

            controller.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}