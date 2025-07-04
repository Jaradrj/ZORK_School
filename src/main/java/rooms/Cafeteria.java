package rooms;

import game.Player;
import game.Room;

import java.util.HashMap;
import java.util.Map;
import game.Exit;
import game.RoomFactory;

import game.Exit;
import game.RoomFactory;

public class Cafeteria implements Room {

    @Override
    public String getName() {
        return "cafeteria";
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_cafeteria")) {
            player.setFlag("was_cafeteria");
            System.out.println("You enter the Cafeteria.");
            System.out.println("Apart from dirty tables, chairs and leftovers on the food distribution counter, there's not much no find here. Behind the iron door is the kitchen.\nNo wonder we are constantly in the media for our hygiene regulations. About 50% of students have at least had one food poisoning.\nThe only thing shining is a safe. We need a key.");
        }
        System.out.println("Actions:");

        if (player.hasFlag("key_taken")) {
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
                    return "I'm going to be rich! That's what's going through your head while your twisting around the key. Not a single buckle. Wrong key.";
                }
                return "The safe still won't work... :(";

            case "leave":
                player.setFlag("leave_cafeteria");
                System.out.println("You decided to leave, where would you like to go to (Use: go to x)");
                System.out.println("You can go to: ");
                System.out.println("- IT Room");
                System.out.println("- Music Room");
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