package rooms;

import game.*;
import java.util.*;

public class MainEntranceRoom implements Room {
    private boolean hasTakenMap = false;

    @Override
    public String getName() {
        return "Main Entrance";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You are in the Main Entrance. It's dark.");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "turn on light":
                return "The lights don’t work.";
            case "sit down":
                player.setFlag("hasNote");
                return "You found a note under the table.";
            case "take map":
                if (!hasTakenMap) {
                    hasTakenMap = true;
                    player.setFlag("hasHalfMap");
                    return "You picked up half of the school map.";
                }
                return "There is no map left.";
            default:
                return "Nothing happens.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("hasHalfMap")) {
            exits.put("Music Room", new Exit("Music Room", null));
            exits.put("Teacher Room", new Exit("Teacher Room", null));
            exits.put("IT Room", new Exit("IT Room", null));
        }
        return exits;
    }
}
