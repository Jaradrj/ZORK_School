package rooms;

import controller.GameController;
import game.*;

import java.util.*;

public class MainEntranceRoom implements Room {

    private final Commands commands;

    @Override
    public String getName() {
        return "main entrance hall";
    }

    public MainEntranceRoom(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_main")) {
            player.setFlag("was_main");
            System.out.println("Your story starts in the main building. There’s not much to find here. You take a look around and notice a few objects.\n" +
                    "Although it's pretty dark, you can still make out some tables, the school's dull pinboard, a light switch, " +
                    "and chairs—some of which have been knocked over.");
            System.out.println("What do you want to do?");
        }
        System.out.println("\nActions:");
        System.out.println("- Turn on the Light");
        System.out.println("- Sit Down at a Table");
        System.out.println("- Examine the Pinboard");

        if (player.hasFlag("half_map_taken")) {
            commands.checkInputCommands("-r", player);
        }
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        if (action.startsWith("go to ")) {
            return handleRoomChange(player, action.substring(6).trim());
        }

        switch (action) {
            case "turn on the light":
            case "light":
                if (!player.hasFlag("lights_tried")) {
                    player.setFlag("lights_tried");
                    return "\nYou flip the switch. Nothing happens. The power must be out. Maybe you need to restore it elsewhere.";
                }
                return "Still no power. The switch is unresponsive.";

            case "sit down at a table":
            case "sit":
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
                return "\nYou’ve already read the note. There's nothing else under the table.";

            case "examine the pinboard":
            case "pinboard":
                if (!player.hasFlag("half_map_taken")) {
                    player.setFlag("half_map_taken");
                    player.getInventory().addItem("Schools half map");

                    return "\nAmong the generic school announcements, you find something useful: half of a torn school map. New rooms unlocked!\n" +
                            "(You can now go to: Music Room, Teacher Room, IT Room)";
                }
                return "You already took the half map from the pinboard.";

            default:
                return "Invalid action. Try 'turn on the light', 'sit down at a table', 'examine the pinboard', or 'go to X'.";
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
            exits.put("it room", new Exit("it Room", null));
        }
        return exits;
    }
}