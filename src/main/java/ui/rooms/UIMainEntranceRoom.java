package ui.rooms;

import ui.audio.TypingEffect;
import ui.game.UICommands;
import ui.game.UIRoom;
import console.game.*;

import java.util.*;

public class UIMainEntranceRoom implements UIRoom {

    private final UICommands commands;

    public UIMainEntranceRoom(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "main entrance hall";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_main")) {
            player.setFlag("was_main");
            text.append("Your story starts in the main building. \nThere’s not much to find here. \nYou take a look around and notice a few objects.\n")
                    .append("Although it's pretty dark, you can still make out some tables, \nthe school's dull pinboard, a light switch,\nand chairs, some of which have been knocked over.\n")
                    .append("What do you want to do?\n");
        } else {
            text.append("You are in the main entrance hall.\n");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (!player.hasFlag("turned_on_power")) {
            actions.add("Turn on the Light");
        }
        actions.add("Sit Down at a Table");
        actions.add("Examine the Pinboard");

        if (player.hasFlag("half_map_taken")) {
            actions.add("Go to Music Room");
            actions.add("Go to Teacher Room");
            actions.add("Go to IT Room");
        }
        return actions;
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        if (action.startsWith("go to ")) {
            return handleRoomChange(player, action.substring(6).trim());
        }

        switch (action) {
            case "turn on the light":
                if (!player.hasFlag("lights_tried")) {
                    player.setFlag("lights_tried");
                    return "You flip the switch. Nothing happens. The power must be out. Maybe you need to restore it elsewhere.";
                }
                return "Still no power. The switch is unresponsive.";

            case "sit down at a table":
                if (!player.hasFlag("hasReadNote")) {
                    player.setFlag("hasReadNote");
                    return "You sit and notice a folded piece of paper under the table:\n\n" +
                            "\"It doesn't start with the light.\n" +
                            "It never starts with the light.\n\n" +
                            "They say it's just an exercise.\n" +
                            "A test.\n" +
                            "A simulation.\n\n" +
                            "But why doesn't anyone talk about those who are no longer there?\n" +
                            "Why is the room always empty, but the feeling never?\n\n" +
                            "I've seen the door. The real one.\n" +
                            "Not the wooden one. The other one – the living one.\n\n" +
                            "If you're reading this:\n" +
                            "Go. Now.\n" +
                            "Or stay... and become like us\"\n\n" +
                            "~ Leano\n\n" +
                            "Leano B. was known for rebelling against the system. He questioned the disappearances, especially after his friend vanished.\n" +
                            "One day, Klara—the class leader—told everyone he'd been expelled. No one's heard from him since.";
                }
                return "You’ve already read the note. There's nothing else under the table.";

            case "examine the pinboard":
                if (!player.hasFlag("half_map_taken")) {
                    player.setFlag("half_map_taken");
                    player.getInventory().addItem("Schools half map");
                    return "Among the generic school announcements, you find something useful: half of a torn school map. New rooms unlocked!\n" +
                            "(You can now go to: Music Room, Teacher Room, IT Room)";
                }
                return "You already took the half map from the pinboard.";

            default:
                return "Invalid action. Try one of the available buttons.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        if (!player.hasFlag("half_map_taken")) {
            return "You don't know where to go yet. Find a map first.";
        }
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There's no room called '" + roomName + "'.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("half_map_taken")) {
            exits.put("music room", new Exit("music room", null));
            exits.put("teacher room", new Exit("teacher room", null));
            exits.put("it room", new Exit("it room", null));
        }
        return exits;
    }
}
