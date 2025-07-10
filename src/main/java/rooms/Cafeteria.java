package rooms;

import game.*;

import java.util.HashMap;
import java.util.Map;

public class Cafeteria implements Room {

    private Commands commands;

    @Override
    public String getName() {
        return "cafeteria";
    }

    public Cafeteria(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_cafeteria")) {
            player.setFlag("was_cafeteria");
            System.out.println("Apart from dirty tables, chairs and leftovers on the food distribution counter, there's not much no find here.\n" +
                    "Behind the iron door is the kitchen.\nNo wonder we are constantly in the media for our hygiene regulations.\n" +
                    "About 50% of students have at least had one food poisoning.\nThe only thing shining is a safe. We need a key.");
        }
        System.out.println("\nActions:");

        if (player.hasFlag("keys_taken")) {
            System.out.println("- Try opening Safe");
        }
        System.out.println("- Leave");

    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "try":
            case "try opening safe":
                if (!player.hasFlag("tried_opening_safe")) {
                    player.setFlag("tried_opening_safe");
                    return "I'm going to be rich!\n" +
                            "That's what's going through your head while your twisting around the key.\n" +
                            "Not a single buckle. Wrong key.";
                }
                return "\nThe safe still won't work... :(";

            case "leave":
                player.setFlag("leave_cafeteria");
                commands.checkInputCommands("-r", player);
                return "";

            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You enter the " + roomName + ".";
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