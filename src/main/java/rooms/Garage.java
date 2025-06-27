package rooms;

import game.*;
import java.util.*;

public class Garage implements Room {

    @Override
    public String getName() {
        return "Garage";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the Garage.");

            if (!player.hasFlag("key_taken")) {
                System.out.println("While watching Mrs. Hamps go to her car, you're hiding behind a big pillar. Suddenly, she stops, tries to grad something from her pocket,\nturns around and is now moving towards you. You manage to keep hidden. It seems that she forgot something. What does she have in that car?");
            }
            System.out.println("Actions:");

            if (!player.hasFlag("key_taken")) System.out.println("- Break into her car");
            System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        switch (action) {
            case "1":
            case "break":
            case "break into her car":
                player.setFlag("keys_taken");
                System.out.println("You need to know what she's hiding. Thank god she didn't suspect anyone to sneak around and left her car unlocked.\nEasy game for you to get inside and start searching quick. You don't find much, just some mints, money, a coke and.. keys!\nBut what are they for?");

            case "leave":
                return "You decide to leave. Where do you want to go? (Use: go to X)";

            default:
                if (action.startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName.toLowerCase())) {
            player.setCurrentRoom(roomName);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("teacher room", new Exit("Teacher Room", null));
        return exits;
    }
}
