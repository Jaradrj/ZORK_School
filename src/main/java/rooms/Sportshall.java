package rooms;

import game.*;
import java.util.HashMap;
import java.util.Map;

public class Sportshall implements Room {

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
        }
        return action;
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        return exits;
    }
}