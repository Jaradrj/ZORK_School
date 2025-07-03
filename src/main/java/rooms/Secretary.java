package rooms;

import game.*;
import java.util.HashMap;
import java.util.Map;

public class Secretary implements Room {

    @Override
    public String getName() {
        return "secretary";
    }

    @Override
    public void enter(Player player) {

        System.out.println("You enter the Secretary.");

        if (!player.hasFlag("was_secretary")) {
            player.setFlag("was_secretary");
            System.out.println("This is probably the most boring room. There's just one big desk that belongs to the Head Teacher. The desk is unusually clean. Just some sticky notes. Wait!\nThere's a big pinboard. We could use some light here to check it out.");
        }
        if (player.hasFlag("flashlight_taken") || player.hasFlag("turned_on_power")) {
            System.out.println("- Examine the Pinboard");
        }

        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "examine":
            case "examine the pinboard":
                System.out.println("Still not much to see, but the flashlight will do its job.");
                if (!player.hasFlag("full_map_taken")) {
                    player.setFlag("full_map_taken");
                    System.out.println("Wait what's that? The full card! You take it. New rooms unlocked!");
                }
                if (player.hasFlag("entered_electricity")) {
                    player.setFlag("police_number_taken");
                    System.out.println("Wait, there's more! You take the Note with the Police's Number on it from the pinboard.");
                }
                return "You examined the pinboard, time to leave.";
            case "leave":
            case "2":
                System.out.println("You decide to leave. Where do you want to go? (Use: go to X)\n\n");
                System.out.println("You can now go to: ");
                System.out.println("- Teacher Room");
                if (player.hasFlag("full_map_taken")) {
                    System.out.println("- Chemistry Room");
                    System.out.println("- Sportshall");
                }
                return "";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
        }
        return "Invalid action.";
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
        exits.put("teacher room", new Exit("teacher room", null));
        if (player.hasFlag("full_map_taken")) {
            exits.put("chemistry room", new Exit("chemistry room", null));
            exits.put("sportshall", new Exit("sportshall", null));
        }
        return exits;
    }
}