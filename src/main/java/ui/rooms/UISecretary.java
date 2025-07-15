package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import org.w3c.dom.Text;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.controller.UIGameController;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UISecretary implements UIRoom {

    private UICommands commands;

    @Override
    public String getName() {
        return "secretary";
    }

    public UISecretary(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();

        if (!player.hasFlag("was_secretary")) {
            player.setFlag("was_secretary");
            text.append("This is probably the most boring room.\nThere's just one big desk that belongs to the Head Teacher. ")
                    .append("\nThe desk is unusually clean. Just some sticky notes. Wait!\nThere's a big pinboard.\nWe could use some light here to check it out.");
        }
        if (player.hasFlag("entered_electricity")) {
            text.append("Maybe we'll find something interesting here...");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("flashlight_taken") || player.hasFlag("turned_on_power")) {
            actions.add("Examine the Pinboard");
        }
        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {

        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "examine the pinboard":
                result.append("Still not much to see, but the flashlight will do its job.");
                if (!player.hasFlag("full_map_taken")) {
                    player.setFlag("full_map_taken");
                    player.getInventory().addItem("Schools half map 2");
                    result.append("Wait what's that? The full card! You take it. New rooms unlocked!\n");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 1000, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else if (player.hasFlag("entered_electricity")) {
                    player.setFlag("police_number_taken");
                    player.getInventory().addItem("Polices number");
                    result.append("Here we are again, seems to be nothing new. But wait, there's more!\nYou take the Note with the Police's\nNumber on it from the pinboard.");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 1000, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else {
                    result.append("You already examined the pinboard, time to leave.");
                }
                break;

            case "leave":
                return "";

            default:
                result.append("Invalid action.");
                break;
        }
        TypingEffect.typeWithSound(outputArea, result.toString(), UIGameController.getGuiInstance(), null);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("teacher room", new Exit("teacher room", null));
        if (player.hasFlag("full_map_taken")) {
            exits.put("chemistry room", new Exit("chemistry room", null));
            exits.put("sportshall", new Exit("sportshall", null));
        }
        return exits;
    }
}