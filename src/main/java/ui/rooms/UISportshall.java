package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UISportshall implements UIRoom {

    private UICommands commands;

    @Override
    public String getName() {
        return "sportshall";
    }

    public UISportshall(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();

        if (!player.hasFlag("was_sports")) {
            player.setFlag("was_sports");
            text.append("It smells weird in here. Not the type of weird you find in a boys wardrobe,\nbut rather the type of weird you smell in basements. ")
                    .append("\nThe smell seems like it's coming from the shaft.\nWhy is it open? There must be a way to get up there. ");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("was_electricity")) {
            actions.add("Use Bench");
        } else {
            actions.add("Move bench");
        }
        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();
        switch (lowerAction) {
            case "move bench":
            case "use bench":
                player.setFlag("entered_electricity");
                if (!player.hasFlag("was_electricity")) {
                    SoundPlayer.playSound("/sounds/MoveBench.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                }
                return handleRoomChange(player, "electricity room");
            case "leave":
                return "";
            default:
                result.append("Invalid action.");
                break;
        }

        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
    }

    @Override
    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return targetRoom.enter(player);
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("secretary", new Exit("secretary", null));
        if (player.hasFlag("entered_electricity")) {
            exits.put("electricity room", new Exit("electricity room", null));
        }
        return exits;
    }
}