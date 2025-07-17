package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.Exit;
import console.game.Player;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.controller.UIGameController;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIGarage implements UIRoom {

    @Override
    public String getName() {
        return "garage";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_garage")) {
            player.setFlag("was_garage");

            if (!player.hasFlag("keys_taken")) {
                text.append("While watching ");
                if (player.hasFlag("knows_teacher_name")) {
                    text.append("Mrs. Hamps ");
                } else {
                    text.append("the woman ");
                }
                text.append("go to her car, you're hiding behind a big pillar.\n")
                        .append("Suddenly, she stops, tries to grab something from her pocket,\n")
                        .append("turns around and is now moving towards you. You manage to keep hidden. It seems that she forgot something.\n")
                        .append("What does she have in that car?");
            }
        } else {
            text.append("You enter the garage");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (!player.hasFlag("keys_taken") && !player.hasFlag("was_garage")) {
            actions.add("Break into her car");
        }
        actions.add("Return to teacher room");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "1":
            case "break":
            case "break into her car":
                if (!player.hasFlag("keys_taken")) {
                    player.setFlag("keys_taken");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 10000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    player.getInventory().addItem("keys (to what?)");
                    result.append("You need to know what she's hiding. Thank god she didn't suspect\nanyone to sneak around and left her car unlocked.\n")
                            .append("Easy game for you to get inside and start searching quick.\nYou don't find much, just some mints, money, a coke and.. keys!\n")
                            .append("But what are they for?");
                } else {
                    result.append("You already broke in to her car, time to leave!");
                }
                break;
            case "return":
            case "return to teacher room":
                player.setFlag("teacher_room_loot_ready");
                return handleRoomChange(player, "teacher room");

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
            return "";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("teacher room", new Exit("teacher room", null));
        return exits;
    }
}