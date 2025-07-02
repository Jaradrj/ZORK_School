package rooms;

import game.*;

import java.util.HashMap;
import java.util.Map;

public class ElectricityRoom implements Room {

    @Override
    public String getName() {
        return "Electricity Room";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the Electricity Room");
        player.setFlag("enter_electricity");
        System.out.println("Actions: ");
        System.out.println(" - Open the door\n - Inspect body\n - Enable radiator");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "open door":
                if (player.hasFlag("key_taken")) {
                    return "You successfully open the door. Where do you want to go? (Use: go to X)";
                } else {
                    return "You try to open the door but rapidly notice that you are missing the one key that might give you your freedom.";
                }
            case "2":
            case "enable radiator":
                if (player.hasFlag("turned_on_power")) {
                    return "You already turned on the power";
                } else {
                    player.setFlag("turned_on_power");
                    return "Turned On Power";
                }
            case "3":
            case "inspect body":
                player.setFlag("body_inspected");
            case "get phone":
                if (player.hasFlag("body_inspected")) {
                    if (!player.hasFlag("phone_taken")) {
                        player.setFlag("phone_taken");
                    } else {
                        return "You already picked up the phone";
                    }
                } else {
                    return "You need to find the phone first";
                }
            case "read note":
                if (player.hasFlag("body_inspected")) {
                    return "note";
                } else {
                    return "You need to find the note first";
                }
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "invalid action";
        }

        return action;
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if(player.hasFlag("found_trash_id")) {
        exits.put("secretary", new Exit("enter_secretary", player));
        }
        exits.put("it", new Exit("enter_it", player));
        return exits;
    }

    @Override
    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName)) {
            player.setCurrentRoom(roomName);
            return "You entered the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }
}
