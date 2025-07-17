package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.controller.UIGameController;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UICafeteria implements UIRoom {

    private UICommands commands;

    @Override
    public String getName() {
        return "cafeteria";
    }

    public UICafeteria(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_cafeteria")) {
            player.setFlag("was_cafeteria");
            text.append("Apart from dirty tables, chairs and leftovers on the food distribution counter, there's not much no find here.\n" +
                    "Behind the iron door is the kitchen.\nNo wonder we are constantly in the media for our hygiene regulations.\n" +
                    "About 50% of students have at least had one food poisoning.\nThe only thing shining is a safe. We need a key.");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("keys_taken")) {
            actions.add("Try opening Safe");
        }
        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {

        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "try opening safe":
                if (!player.hasFlag("tried_opening_safe")) {
                    player.setFlag("tried_opening_safe");
                    SoundPlayer.playSound("/sounds/OpenSafe.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("I'm going to be rich!\n" +
                            "That's what's going through your head while your twisting around the key.\n" +
                            "Not a single buckle. Wrong key.");
                } else {
                    result.append("\nThe safe still won't work... :(");
                }
                break;

            case "leave":
                player.setFlag("leave_cafeteria");
                return "";
            default:
                result.append("Invalid action.");
                break;
        }
        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return "";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("music room", new Exit("music room", null));
        exits.put("it room", new Exit("it room", null));
        return exits;
    }
}