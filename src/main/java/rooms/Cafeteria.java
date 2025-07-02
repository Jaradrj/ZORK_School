package rooms;

import game.Player;
import game.Room;
import java.util.HashMap;
import java.util.Map;
import game.Exit;

public class Cafeteria implements Room {

    @Override
    public String getName() {
        return "Cafeteria";
    }

    @Override
    public void enter(Player player) {
        if(!player.hasFlag("was_cafeteria")) {
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
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName.toLowerCase())) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits (Player player){
        Map<String, Exit> exits = new HashMap<>();
        exits.put("music room", new Exit("Music Room", null));
        exits.put("it room", new Exit("IT Room", null));
        return exits;
    }
}